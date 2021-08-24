package me.matzhilven.war.commands.subcommands.war;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class WarSubCommand implements SubCommand {

    private final WARPlugin main;

    public WarSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (args.length != 4) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-admin"));
            return;
        }

        if (main.getCurrentWar() != null) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("ongoing-war"));
            return;
        }

        if (!main.getClanManager().getClanByName(args[1]).isPresent() ||
        !main.getClanManager().getClanByName(args[2]).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clan"));
            return;
        }

        Clan clan1 = main.getClanManager().getClanByName(args[1]).get();
        Clan clan2 = main.getClanManager().getClanByName(args[2]).get();

        if (clan1 == clan2) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clans"));
            return;
        }

        int time;

        try {
            time = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-time"));
            return;
        }

        if (time <= 0) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-time"));
            return;
        }

        War war = new War(clan1, clan2, time);
        war.runTaskTimer(main, 0L, 20L);

        main.setCurrentWar(war);

        StringUtils.sendMessage(sender, main.getMessages().getString("started-war")
            .replace("%clan-1%", clan1.getName())
            .replace("%clan-2%", clan2.getName())
        );

    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
