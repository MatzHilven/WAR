package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HelpSubCommand implements SubCommand {

    private final WARPlugin main;

    public HelpSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        StringUtils.sendMessage(sender, main.getMessages().getStringList("usage"));
    }

    @Override
    public String getPermission() {
        return null;
    }
}
