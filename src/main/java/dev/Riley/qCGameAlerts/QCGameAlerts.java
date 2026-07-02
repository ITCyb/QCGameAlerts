package dev.Riley.qCGameAlerts;
import dev.Riley.qCGameAlerts.Commands.qcgaWorldGuard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import dev.Riley.qCGameAlerts.Commands.qcgaAnnounce;
import dev.Riley.qCGameAlerts.Commands.qcgaCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import com.sk89q.worldguard.protection.flags.Flag;

public final class QCGameAlerts extends JavaPlugin {
    @Override
    public void onLoad() {
        try {
            WorldGuard.getInstance()
                    .getFlagRegistry()
                    .register(qcgaWorldGuard.QCGA_GAME_FLAG);
        } catch (FlagConflictException | IllegalStateException e) {
            getLogger().warning("qcga-game flag already registered.");
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(
            new qcgaWorldGuard(this, new qcgaAnnounce(this)),
            this
    );
        int version = getConfig().getInt("config-version");

        if (version < 3) {
            getConfig().set("normal-message",
                    "<yellow>%player%</yellow> has joined <yellow>%gametype%</yellow>! Join them at <green>/warp games</green>");
            getConfig().set("cooldown-message",
                    "A player has joined <yellow>%gametype%</yellow>! Join them at <green>/warp games</green>");
            getConfig().set("config-version", 3);
            saveConfig();
        }
        // Plugin startup logic
        saveDefaultConfig();
        Bukkit.getLogger().info( "QCGameAlerts Enabled");

        this.getCommand("qcga").setExecutor(new qcgaCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info( "QCGameAlerts shutting down.");
    }

    public String getPrefix() {
        return getConfig().getString("prefix", "");
    }
}
