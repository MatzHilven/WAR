package me.matzhilven.war.commands.subcommands.war;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class EndWarSubCommand implements SubCommand {

    private final WARPlugin main;

    public EndWarSubCommand(WARPlugin main) {
        this.main = main;
    }


    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (main.getCurrentWar() == null) {
            StringUtils.sendMessage(sender, main.getMessages().getString("no-ongoing-war"));
            return;
        }

        War war = main.getCurrentWar();
        war.end();
        StringUtils.sendMessage(sender, main.getMessages().getString("war-ended"));
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
