package qwq.qwqifya.qwqifyaClient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
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
        CommandManager.refreshCommands();
    }
}
