package me.matzhilven.war.commands.subcommands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsSubCommand implements SubCommand {

    private final WARPlugin main;

    public StatsSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (!main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
            return;
        }

        Clan clan = main.getClanManager().getClan(sender).get();

        List<String> infoList = main.getMessages().getStringList("stats");

        List<String> finalList = new ArrayList<>();

        for (String info : infoList) {
            info = info.replace("%clan%", clan.getName());
            info = info.replace("%wins%", StringUtils.format(clan.getWins()));
            info = info.replace("%losses%", StringUtils.format(clan.getLosses()));
            info = info.replace("%kills%", StringUtils.format(clan.getKills()));
            finalList.add(info);
        }

        StringUtils.sendMessage(sender, finalList);

    }

    @Override
    public String getPermission() {
        return null;
    }
}
