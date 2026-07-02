package dev.Riley.qCGameAlerts.Commands;

import dev.Riley.qCGameAlerts.QCGameAlerts;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Arrays;

public class qcgaCommand implements CommandExecutor {

    private final qcgaAnnounce announce;
    private final qcgaReload reload;
    private final qcgaCooldown cooldown;
    private final qcgaCooldownTime cooldownTime;

    public qcgaCommand(QCGameAlerts plugin) {
        this.announce = new qcgaAnnounce(plugin);
        this.reload = new qcgaReload(plugin);
        this.cooldown = new qcgaCooldown(plugin);
        this.cooldownTime = new qcgaCooldownTime(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (args.length == 0) {
            sender.sendMessage("/qcga announce <player> <game>");
            sender.sendMessage("/qcga reload");
            sender.sendMessage("/qcga cooldown <on|off>");
            sender.sendMessage("/qcga time <time in seconds>");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "reload":
                return reload.execute(sender);

            case "announce":

                if (args.length < 3) {
                    sender.sendMessage(
                            "/qcga announce <player> <game>"
                    );
                    return true;
                }
                String targetName = args[1];

                String gameType = String.join(" ",
                        Arrays.copyOfRange(args, 2, args.length));

                return announce.execute(
                        sender,
                        targetName,
                        gameType
                );

            case "cooldown":

                if (!sender.hasPermission("qcga.cooldown")) {
                    sender.sendMessage("No permission.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage("/qcga cooldown <on|off>");
                    return true;
                }

                switch (args[1].toLowerCase()) {

                    case "on":
                        return cooldown.execute(sender, true);

                    case "off":
                        return cooldown.execute(sender, false);

                    default:
                        sender.sendMessage("/qcga cooldown <on|off>");
                        return true;
                }

            case "time":

                if (!sender.hasPermission("qcga.cooldown.time")) {
                    sender.sendMessage("No permission.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage("/qcga time <seconds>");
                    return true;
                }

                int seconds;

                try {
                    seconds = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("You must enter a valid number.");
                    return true;
                }

                if (seconds < 1) {
                    sender.sendMessage("Cooldown must be at least 1 second.");
                    return true;
                }

                return cooldownTime.execute(sender, seconds);

            default:
                sender.sendMessage("Unknown subcommand.");
                return true;
        }
    }
}