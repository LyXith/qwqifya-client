package qwq.qwqifya.qwqifyaClient.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.List;

import static qwq.qwqifya.qwqifyaClient.QwQifyaClient.msgManager;

public class CheckData {
    public static boolean checkClickEvent(BlockPos blockPos, Level level, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SignBlockEntity signBlockEntity) {
            SignText text = signBlockEntity.isFacingFrontText(player) ? signBlockEntity.getFrontText() : signBlockEntity.getBackText();
            if (text.hasAnyClickCommands(player)) {
                msgManager.sendMsg(player,Component.literal("已阻止指令运行，指令内容："));
                for(Component component : text.getMessages(player.isTextFilteringEnabled())) {
                    Style style = component.getStyle();
                    ClickEvent clickEvent = style.getClickEvent();
                    if (clickEvent != null && clickEvent.action() == ClickEvent.Action.RUN_COMMAND) {
                        String command = ((ClickEvent.RunCommand) clickEvent).command();
                        msgManager.sendMsg(player,Component.literal(command));
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static boolean checkEntityData(ItemStack item, Player player) {
        DataComponentMap itemData = item.getComponents();
        TypedEntityData<?> entityData = itemData.get(DataComponents.ENTITY_DATA);
        if (entityData == null) {return true;}
        if (item.getItem() instanceof SpawnEggItem) {
            if (entityData.type() == EntityType.FALLING_BLOCK) {
                return checkFallingBlockBlockEntityData(entityData, player);
            }
            if (entityData.type() == EntityType.COMMAND_BLOCK_MINECART) {
                String command = entityData.copyTagWithoutId().getStringOr("Command","");
                if (!command.isEmpty()) {
                    msgManager.sendMsg(player,Component.literal("检测到命令方块矿车，指令为" + command + ",已阻止放置"));
                    return false;
                }
            }
        }
        if (item.getItem() == Items.COMMAND_BLOCK_MINECART) {
            String command = entityData.copyTagWithoutId().getStringOr("Command","");
            if (!command.isEmpty()) {
                msgManager.sendMsg(player,Component.literal("检测到命令方块矿车，指令为" + command + ",已阻止放置"));
                return false;
            }
        }
        return true;
    }

    public static boolean checkFallingBlockBlockEntityData(TypedEntityData<?> entityData, Player player) {
        CompoundTag entityTag = entityData.copyTagWithoutId();
        CompoundTag blockState = entityTag.getCompoundOrEmpty("BlockState");
        String blockId = blockState.getStringOr("Name", "");
        if (blockId.endsWith("command_block")) {
            CompoundTag blockEntityData = entityTag.getCompoundOrEmpty("TileEntityData");
            if (!blockEntityData.getStringOr("Command", "").isEmpty()
                    && blockEntityData.getBooleanOr("auto", false)) {
                String command = blockEntityData.getStringOr("Command", "");
                msgManager.sendMsg(player,Component.literal("检测到下落的命令方块，指令为" + command + ",已阻止放置"));
                return false;
            }
        }
        return true;
    }

    public static boolean checkBlockEntityData(ItemStack item, Player player) {
        DataComponentMap itemData = item.getComponents();
        TypedEntityData<BlockEntityType<?>> blockEntityData = itemData.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null) {
            String command = blockEntityData.copyTagWithoutId().getStringOr("Command","");
            if (!command.isEmpty() && blockEntityData.copyTagWithoutId().getBooleanOr("auto",false)) {
                msgManager.sendMsg(player,Component.literal("检测到自动执行的指令：" + command + ",已阻止放置"));
                return false;
            }
        }
        return true;
    }

    public static boolean checkEffect(ItemStack item, Player player) {
        if (item.getItem() == Items.POTION) {
            DataComponentMap itemData = item.getComponents();
            PotionContents potionContents = itemData.get(DataComponents.POTION_CONTENTS);
            if (potionContents != null) {
                List<MobEffectInstance> effects = potionContents.customEffects();
                for (MobEffectInstance effect : effects) {
                    if (effect.getEffect() == MobEffects.INSTANT_HEALTH && effect.getAmplifier() == 125) {
                        msgManager.sendMsg(player,Component.literal("已阻止使用药水"));
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
