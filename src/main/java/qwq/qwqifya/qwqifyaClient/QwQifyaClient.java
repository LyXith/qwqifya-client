package qwq.qwqifya.qwqifyaClient;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import qwq.qwqifya.qwqifyaApi.commandUtils.CommandManager;
import qwq.qwqifya.qwqifyaApi.messageUtils.MsgManager;
import qwq.qwqifya.qwqifyaClient.utils.ClientConfig;

import static qwq.qwqifya.qwqifyaClient.utils.CheckData.*;

public class QwQifyaClient implements ClientModInitializer {
    public final static String modId =  "qwqifya-client";
    public static ClientConfig config = null;
    public static MsgManager msgManager = new MsgManager(modId);
    @Override
    public void onInitializeClient() {

        AutoConfig.register(ClientConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();

        UseBlockCallback.EVENT.register((player, level, hand, blockHitResult) -> {
            if (!config.clickEventEnabled) {
                boolean stopClickEvent = !checkClickEvent(blockHitResult.getBlockPos(), level, player);
                if (stopClickEvent) {
                    player.swing(hand);
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.PASS;
        });
        BlockEvents.USE_ITEM_ON.register((item, blockState, level, blockPos, player, hand, blockHitResult) ->
        {
            if (config.checkEntityData) {
                if (!checkEntityData(item, player)) {
                    player.swing(hand);
                    return InteractionResult.FAIL;
                }
            }
            if  (config.checkBlockEntityData) {
                if (!checkBlockEntityData(item, player)) {
                    player.swing(hand);
                    return InteractionResult.FAIL;
                }
            }
            return null;
        });
        UseItemCallback.EVENT.register((player, world, hand) ->{
            ItemStack item = player.getMainHandItem();
            if (config.checkPotionEffects) {
                if (!checkEffect(item, player)) {
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.PASS;
        });
        CommandManager.refreshCommands();

    }
}
