package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatsSubCommand implements SubCommand {

    private final WARPlugin main;

    public StatsSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (args.length == 1) {
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
        } else {
            Optional<Clan> clan = main.getClanManager().getClanByName(args[1]);

            if (!clan.isPresent()) {
                StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clan"));
                return;
            }

            List<String> infoList = main.getMessages().getStringList("stats");

            List<String> finalList = new ArrayList<>();

            for (String info : infoList) {
                info = info.replace("%clan%", clan.get().getName());
                info = info.replace("%wins%", StringUtils.format(clan.get().getWins()));
                info = info.replace("%losses%", StringUtils.format(clan.get().getLosses()));
                info = info.replace("%kills%", StringUtils.format(clan.get().getKills()));
                finalList.add(info);
            }

            StringUtils.sendMessage(sender, finalList);
        }


    }

    @Override
    public String getPermission() {
        return null;
    }
}
