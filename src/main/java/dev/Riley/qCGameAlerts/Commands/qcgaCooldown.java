package dev.Riley.qCGameAlerts.Commands;

import dev.Riley.qCGameAlerts.QCGameAlerts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class qcgaCooldown {

    private final QCGameAlerts plugin;
    private final MiniMessage miniMsg = MiniMessage.miniMessage();

    public qcgaCooldown(QCGameAlerts plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, boolean state) {

        if (sender.hasPermission("qcga.cooldown")) {
            plugin.getConfig().set("cooldown-mode", state);
            plugin.saveConfig();

            Component msg = miniMsg.deserialize(
                    plugin.getPrefix() + "Cooldown mode set to: " + state
            );

            sender.sendMessage(msg);

            return true;
        } else {
            Component msg = miniMsg.deserialize(plugin.getPrefix() + "<red>You don't have permission to use this command</red>");
            sender.sendMessage(msg);
            plugin.getLogger().warning(sender.getName() + " does not have permission to use this command");
            return true;
        }
    }
}