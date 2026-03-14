package qwq.qwqifya.qwqifyaClient.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

public class CheckData {
    public static boolean checkClickEvent(BlockPos blockPos, Level level, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SignBlockEntity signBlockEntity) {
            SignText text = signBlockEntity.isFacingFrontText(player) ? signBlockEntity.getFrontText() : signBlockEntity.getBackText();
            if (text.hasAnyClickCommands(player)) {
                player.displayClientMessage(Component.literal("已阻止指令运行，指令内容："),false);
                for(Component component : text.getMessages(player.isTextFilteringEnabled())) {
                    Style style = component.getStyle();
                    ClickEvent clickEvent = style.getClickEvent();
                    if (clickEvent != null && clickEvent.action() == ClickEvent.Action.RUN_COMMAND) {
                        String command = ((ClickEvent.RunCommand) clickEvent).command();
                        player.displayClientMessage(Component.literal(command),false);
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
                return checkBlockEntityData(entityData, player);
            }
            if (entityData.type() == EntityType.COMMAND_BLOCK_MINECART) {
                String command = entityData.copyTagWithoutId().getStringOr("Command","");
                if (!command.isEmpty()) {
                    player.displayClientMessage(Component.literal("检测到命令方块矿车，指令为" + command + ",已阻止放置"), false);
                    return false;
                }
            }
        }
        if (item.getItem() == Items.COMMAND_BLOCK_MINECART) {

        }
        return true;
    }

    public static boolean checkBlockEntityData(TypedEntityData<?> entityData, Player player) {
        CompoundTag entityTag = entityData.copyTagWithoutId();
        CompoundTag blockState = entityTag.getCompoundOrEmpty("BlockState");
        String blockId = blockState.getStringOr("Name", "");
        if (blockId.endsWith("command_block")) {
            CompoundTag blockEntityData = entityTag.getCompoundOrEmpty("TileEntityData");
            if (!blockEntityData.getStringOr("Command", "").isEmpty()
                    && blockEntityData.getBooleanOr("auto", false)) {
                String command = blockEntityData.getStringOr("Command", "");
                player.displayClientMessage(Component.literal("检测到下落的命令方块，指令为" + command + ",已阻止放置"), false);
                return false;
            }
        }
        return true;
    }
}
