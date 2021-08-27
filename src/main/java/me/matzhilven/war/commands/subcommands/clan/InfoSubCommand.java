package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InfoSubCommand implements SubCommand {

    private final WARPlugin main;

    public InfoSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (args.length == 0) {
            if (!main.getClanManager().getClan(sender).isPresent()) {
                StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
                return;
            }

            Clan clan = main.getClanManager().getClan(sender).get();

            List<String> infoList = main.getMessages().getStringList("info");

            List<String> finalList = new ArrayList<>();

            for (String info : infoList) {
                info = info.replace("%clan%", clan.getName());
                info = info.replace("%leader%", Bukkit.getOfflinePlayer(clan.getLeader()).getName());
                info = info.replace("%co_leaders%", getCoLeaders(clan));
                info = info.replace("%members%", getMembers(clan));
                finalList.add(info);
            }

            StringUtils.sendMessage(sender, finalList);
        } else {
            if (args[0].equalsIgnoreCase("info")) {
                Clan clan = main.getClanManager().getClan(sender).get();

                List<String> infoList = main.getMessages().getStringList("info");

                List<String> finalList = new ArrayList<>();

                for (String info : infoList) {
                    info = info.replace("%clan%", clan.getName());
                    info = info.replace("%leader%", Bukkit.getOfflinePlayer(clan.getLeader()).getName());
                    info = info.replace("%co_leaders%", getCoLeaders(clan));
                    info = info.replace("%members%", getMembers(clan));
                    finalList.add(info);
                }

                StringUtils.sendMessage(sender, finalList);
                return;
            }

            Optional<Clan> clan = main.getClanManager().getClanByName(args[1]);

            if (!clan.isPresent()) {
                StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clan"));
                return;
            }

            List<String> infoList = main.getMessages().getStringList("info");

            List<String> finalList = new ArrayList<>();

            for (String info : infoList) {
                info = info.replace("%clan%", clan.get().getName());
                info = info.replace("%leader%", Bukkit.getOfflinePlayer(clan.get().getLeader()).getName());
                info = info.replace("%co_leaders%", getCoLeaders(clan.get()));
                info = info.replace("%members%", getMembers(clan.get()));
                finalList.add(info);
            }

            StringUtils.sendMessage(sender, finalList);
        }


    }

    @Override
    public String getPermission() {
        return null;
    }

    private String getCoLeaders(Clan clan) {
        if (clan.getCoLeaders().size() == 0) return "None";
        return StringUtils.addCommas(clan.getCoLeaders().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                .collect(Collectors.toList()));
    }
    private String getMembers(Clan clan) {
        if (clan.getMembers().size() == 0) return "None";
        return StringUtils.addCommas(clan.getMembers().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                .collect(Collectors.toList()));
    }
}
