package qwq.qwqifya.qwqifyaClient.commands;

import com.mojang.brigadier.tree.CommandNode;
import me.shedaniel.autoconfig.AutoConfigClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import qwq.qwqifya.qwqifyaApi.commandUtils.BaseCommand;
import qwq.qwqifya.qwqifyaApi.commandUtils.RegisterClientCommand;
import qwq.qwqifya.qwqifyaClient.utils.ClientConfig;

import static qwq.qwqifya.qwqifyaClient.QwQifyaClient.configHolder;

public class OpenConfigMenuCmd extends BaseCommand {
    @RegisterClientCommand
    public static CommandNode<FabricClientCommandSource>  openConfigMenu = ClientCommandManager.literal("qwqifya-client")
            .executes(c -> {
                Minecraft mc = c.getSource().getClient();
                if (configHolder != null) {
                    Screen parent = mc.screen;
                    mc.setScreen(AutoConfigClient.getConfigScreen(ClientConfig.class, parent).get());
                }
                return 1;
            }).build();
}
