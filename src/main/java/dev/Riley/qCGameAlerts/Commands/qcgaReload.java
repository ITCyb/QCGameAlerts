package dev.Riley.qCGameAlerts.Commands;

import dev.Riley.qCGameAlerts.QCGameAlerts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class qcgaReload {

    private final QCGameAlerts plugin;
    private final MiniMessage miniMsg = MiniMessage.miniMessage();

    public qcgaReload(QCGameAlerts plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender) {

        if (sender.hasPermission("qcga.reload")) {
            plugin.reloadConfig();

            Component msg = miniMsg.deserialize(
                    plugin.getPrefix() + "OwO Config successfully reloaded! UwU"
            );

            sender.sendMessage(msg);
            return true;
        }
        else {
            Component msg = miniMsg.deserialize(
                    plugin.getPrefix() + "You do not have permission to use this command. 3:<"
            );

            sender.sendMessage(msg);
            return true;
        }
    }
}