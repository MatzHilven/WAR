package me.matzhilven.war.commands.subcommands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
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

        clan.removeMember(sender, false);
        StringUtils.sendMessage(sender, main.getMessages().getString("left-player"));
    }

    @Override
    public String getPermission() {
        return null;
    }
}
