package me.matzhilven.war.commands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.data.PlayerData;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final WARPlugin main;

    public StatsCommand(WARPlugin main) {
        this.main = main;
        main.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            PlayerData playerData = PlayerData.get(p.getUniqueId());

            for (String msg : main.getMessages().getStringList("player-stats")) {
                msg = msg.replace("%player%", p.getName());
                msg = msg.replace("%wins%", StringUtils.format(playerData.getWins()));
                msg = msg.replace("%losses%", StringUtils.format(playerData.getLosses()));
                msg = msg.replace("%kills%", StringUtils.format(playerData.getKills()));
                msg = msg.replace("%deaths%", StringUtils.format(playerData.getDeaths()));
                msg = msg.replace("%kd%", playerData.getKD());
                msg = msg.replace("%killstreak%", StringUtils.format(playerData.getBestKillStreak()));
                StringUtils.sendMessage(p, msg);
            }
        } else {
            if (Bukkit.getPlayer(args[0]) == null) {
                StringUtils.sendMessage(sender, main.getMessages().getString("invalid-player"));
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            PlayerData playerData = PlayerData.get(target.getUniqueId());

            for (String msg : main.getMessages().getStringList("player-stats")) {
                msg = msg.replace("%player%", target.getName());
                msg = msg.replace("%wins%", StringUtils.format(playerData.getWins()));
                msg = msg.replace("%losses%", StringUtils.format(playerData.getLosses()));
                msg = msg.replace("%kills%", StringUtils.format(playerData.getKills()));
                msg = msg.replace("%deaths%", StringUtils.format(playerData.getDeaths()));
                msg = msg.replace("%kd%", playerData.getKD());
                msg = msg.replace("%killstreak%", StringUtils.format(playerData.getBestKillStreak()));
                StringUtils.sendMessage(sender, msg);
            }
        }

        return false;
    }
}
