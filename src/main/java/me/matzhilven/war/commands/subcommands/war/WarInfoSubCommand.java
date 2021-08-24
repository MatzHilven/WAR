package me.matzhilven.war.commands.subcommands.war;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.ClanUtils;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class WarInfoSubCommand implements SubCommand {

    private final WARPlugin main;

    public WarInfoSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (main.getCurrentWar() == null) {
            StringUtils.sendMessage(sender, main.getMessages().getString("no-ongoing-war"));
            return;
        }

        War war = main.getCurrentWar();
        Clan clan1 = war.getClan1();
        Clan clan2 = war.getClan2();

        for (String msg : main.getMessages().getStringList("war-info")) {
            msg = msg.replace("%clan_1%", clan1.getName());
            msg = msg.replace("%clan_2%", clan2.getName());
            msg = msg.replace("%kills_1%", StringUtils.format(war.getKillsClan1()));
            msg = msg.replace("%kills_2%", StringUtils.format(war.getKillsClan2()));
            msg = msg.replace("%deaths_1%", StringUtils.format(war.getDeathsClan1()));
            msg = msg.replace("%deaths_2%", StringUtils.format(war.getDeathsClan2()));
            msg = msg.replace("%online_1%", ClanUtils.getOnlineMembersFormatted(clan1));
            msg = msg.replace("%online_2%", ClanUtils.getOnlineMembersFormatted(clan2));
            msg = msg.replace("%time%", war.getTimeFormatted());
            StringUtils.sendMessage(sender, msg);
        }
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
