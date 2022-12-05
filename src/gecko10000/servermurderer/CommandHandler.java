package gecko10000.servermurderer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CommandHandler implements CommandExecutor {

    private final ServerMurderer plugin;
    private final Predicate<String> regex;

    public CommandHandler(ServerMurderer plugin) {
        this.plugin = plugin;
        this.regex = Pattern.compile("\\d+(?i)[smhd]").asMatchPredicate();
        plugin.getCommand("murder").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return usage(sender);
        String strDuration = args[0];
        if (!regex.test(strDuration)) return usage(sender);
        int lastChar = strDuration.length() - 1;
        long amount = Long.parseLong(strDuration.substring(0, lastChar));
        Duration duration = Duration.of(amount, switch (strDuration.charAt(lastChar)) {
            case 'd' -> ChronoUnit.DAYS;
            case 'h' -> ChronoUnit.HOURS;
            case 'm' -> ChronoUnit.MINUTES;
            default -> ChronoUnit.SECONDS;
        });
        Bukkit.broadcastMessage(ChatColor.GREEN + String.format("Murdering the server for %s.", strDuration));
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Murder complete.");
        return true;
    }

    private boolean usage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /murder <time>");
        sender.sendMessage(ChatColor.RED + "Time may be formatted as \"10s\" or \"6000m\" or \"h\"ours or \"d\"ays.");
        return true;
    }
}
