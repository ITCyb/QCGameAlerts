package dev.Riley.qCGameAlerts.Commands;

import dev.Riley.qCGameAlerts.QCGameAlerts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;


public class qcgaAnnounce {

    private final QCGameAlerts plugin;
    private final MiniMessage miniMsg = MiniMessage.miniMessage();

    private long lastUsedGlobal = 0;

    public qcgaAnnounce(QCGameAlerts plugin) {
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String targetName, String gameType) {

        // Console only
        if (sender instanceof Player) {
            Component msg = miniMsg.deserialize(
                    plugin.getPrefix() +
                            "This command cannot be run as a player. This is intended. Please run again from Console."
            );

            sender.sendMessage(msg);
            return true;
        }

        List<Entity> entities = Bukkit.selectEntities(sender, targetName);

        if (entities.isEmpty()) {
            sender.sendMessage("No player found.");
            return true;
        }

        for (Entity entity : entities) {
            if (entity instanceof Player target) {
                announce(target, gameType);
                return true;
            }
        }

        return true;
    }

    public void announce(Player target, String gameType) {

        boolean cooldownMode = plugin.getConfig().getBoolean("cooldown-mode");

        long now = System.currentTimeMillis();
        int seconds = plugin.getConfig().getInt("cooldown");
        long cooldownMillis = seconds * 1000L;

        if (cooldownMode) {

            if (now - lastUsedGlobal < cooldownMillis) {
                long remaining = (cooldownMillis - (now - lastUsedGlobal)) / 1000;
                plugin.getLogger().info("[QCGA] Wait " + remaining + "s before using again.");
                return;
            }

            lastUsedGlobal = now;

            String msg = plugin.getPrefix()
                    + plugin.getConfig().getString("cooldown-message")
                    .replace("%gametype%", gameType);

            Component component = miniMsg.deserialize(msg);

            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendMessage(component));

            return;
        }

        String msg = plugin.getPrefix()
                + plugin.getConfig().getString("normal-message")
                .replace("%player%", target.getName())
                .replace("%gametype%", gameType);

        Component component = miniMsg.deserialize(msg);

        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(component));
    }
}