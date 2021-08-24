package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class DisbandSubCommand implements SubCommand {

    private final WARPlugin main;

    public DisbandSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (!main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
            return;
        }

        if (!main.getClanManager().getOwnedClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("no-clan-owned"));
            return;
        }

        Clan clan = main.getClanManager().getOwnedClan(sender).get();

        main.getClanManager().removeClan(clan);

        StringUtils.sendMessage(sender, main.getMessages().getString("disbanded-clan")
                .replace("%clan%", clan.getName()));
    }

    @Override
    public String getPermission() {
        return null;
    }
}
