package dev.Riley.qCGameAlerts.Commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.Riley.qCGameAlerts.QCGameAlerts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class qcgaWorldGuard implements Listener {

    private final QCGameAlerts plugin;
    private final qcgaAnnounce announce;

    private final Map<UUID, String> lastRegion = new HashMap<>();

    public static final StringFlag QCGA_GAME_FLAG =
            new StringFlag("qcga-game");

    public qcgaWorldGuard(QCGameAlerts plugin, qcgaAnnounce announce) {
        this.plugin = plugin;
        this.announce = announce;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (event.getTo() == null) {
            return;
        }

        // Ignore movement within the same block.
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        RegionContainer container = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer();

        RegionQuery query = container.createQuery();

        var regions = query.getApplicableRegions(BukkitAdapter.adapt(event.getTo()));

        String currentGameType = regions.getRegions().stream()
                .map(region -> region.getFlag(QCGA_GAME_FLAG))
                .filter(flag -> flag != null && !flag.isEmpty())
                .findFirst()
                .orElse(null);

        String previousGameType = lastRegion.get(uuid);

        // Player left all QCGA regions.
        if (currentGameType == null) {
            lastRegion.remove(uuid);
            return;
        }

        // Still in the same game region.
        if (currentGameType.equals(previousGameType)) {
            return;
        }

        lastRegion.put(uuid, currentGameType);

        Bukkit.getScheduler().runTask(plugin, () ->
                announce.announce(player, currentGameType)
        );
    }
}