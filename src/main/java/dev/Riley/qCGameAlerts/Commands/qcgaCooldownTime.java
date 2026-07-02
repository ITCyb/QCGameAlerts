package dev.Riley.qCGameAlerts.Commands;

import dev.Riley.qCGameAlerts.QCGameAlerts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class qcgaCooldownTime {

    private final QCGameAlerts plugin;
    private final MiniMessage miniMsg = MiniMessage.miniMessage();

    public qcgaCooldownTime(QCGameAlerts plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, int state) {

        if (sender.hasPermission("qcga.cooldown.time")) {
            plugin.getConfig().set("cooldown", state);
            plugin.saveConfig();

            Component msg = miniMsg.deserialize(
                    plugin.getPrefix() + "Cooldown time set to: " + state + " seconds."
            );

            sender.sendMessage(msg);

            return true;
        } else {
            Component msg = miniMsg.deserialize(
              plugin.getPrefix() + "You don't have permission to use this command. 3:<"
            );

            sender.sendMessage(msg);

            return true;
        }
    }
}