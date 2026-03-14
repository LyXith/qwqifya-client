package qwq.qwqifya.qwqifyaClient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import qwq.qwqifya.qwqifyaApi.commandUtils.CommandManager;
import qwq.qwqifya.qwqifyaClient.commands.ControlCommand;

import static qwq.qwqifya.qwqifyaClient.utils.CheckData.*;

public class QwQifyaClient implements ClientModInitializer {
    public static boolean enableClickEvent = false;
    @Override
    public void onInitializeClient() {
        new ControlCommand();
        UseBlockCallback.EVENT.register((player, level, hand, blockHitResult) -> {
            if (!enableClickEvent) {
                boolean stopClickEvent = !checkClickEvent(blockHitResult.getBlockPos(), level, player);
                if (stopClickEvent) {
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.PASS;
        });
        BlockEvents.USE_ITEM_ON.register((item, blockState, level, blockPos, player, interactionHand, blockHitResult) ->
        {
            if (!checkEntityData(item,player) || !checkBlockEntityData(item,player)) {
                return InteractionResult.SUCCESS.withoutItem();
            }
            return null;
        });
        UseItemCallback.EVENT.register((player, world, hand) ->{
            ItemStack item = player.getMainHandItem();
            if (!checkEffect(item,player)) {
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        });
        CommandManager.refreshCommands();

    }
}
