package me.matzhilven.war.commands.subcommands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CreateSubCommand implements SubCommand {

    private final WARPlugin main;

    public CreateSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage"));
            return;
        }

        if (main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("already-in-clan"));
            return;
        }

        Clan clan = new Clan(args[1], sender.getUniqueId());
        main.getClanManager().addClan(clan);

        StringUtils.sendMessage(sender, main.getMessages().getString("created-clan")
        .replace("%clan%", args[1]));
    }

    @Override
    public String getPermission() {
        return null;
    }
}
