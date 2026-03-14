package qwq.qwqifya.qwqifyaClient.commands;

import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import qwq.qwqifya.qwqifyaApi.commandUtils.BaseCommand;
import qwq.qwqifya.qwqifyaApi.commandUtils.RegisterClientCommand;

import static qwq.qwqifya.qwqifyaClient.QwQifyaClient.enableClickEvent;

public class ControlCommand extends BaseCommand {
    @RegisterClientCommand
    public static CommandNode<FabricClientCommandSource> command = ClientCommandManager.literal("stop_click_event")
            .executes(c -> {
                if (enableClickEvent) {
                    c.getSource().sendFeedback(Component.literal("closed"));
                } else {
                    c.getSource().sendFeedback(Component.literal("opened"));
                }
                enableClickEvent = !enableClickEvent;
                return 1;
            }).build();
}
