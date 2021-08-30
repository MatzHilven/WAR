package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class LeaveSubCommand implements SubCommand {

    private final WARPlugin main;

    public LeaveSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (!main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
            return;
        }

        Clan clan = main.getClanManager().getClan(sender).get();

        if (clan.getLeader().toString().equals(sender.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("cannot-leave"));
            return;
        }

        clan.removeMember(sender, false);

        StringUtils.sendMessage(sender, main.getMessages().getString("left-player")
                .replace("%clan%", clan.getName())
        );

        if (main.getCurrentWar() != null) {
            War war = main.getCurrentWar();
            if (war.containsClan(clan)) {
                war.removePlayer(sender);
            }
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}
