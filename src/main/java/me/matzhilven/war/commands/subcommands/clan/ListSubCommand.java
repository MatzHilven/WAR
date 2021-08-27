package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.ClanUtils;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ListSubCommand implements SubCommand {

    private final WARPlugin main;

    public ListSubCommand(WARPlugin main) {
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

            StringUtils.sendMessage(sender, main.getMessages().getString("list")
                    .replace("%clan%", clan.getName()));
            StringUtils.sendMessage(sender, ClanUtils.getMembersFormatted(clan));

        } else {
            Optional<Clan> clan = main.getClanManager().getClanByName(args[1]);

            if (!clan.isPresent()) {
                StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clan"));
                return;
            }

            StringUtils.sendMessage(sender, main.getMessages().getString("list")
                    .replace("%clan%", clan.get().getName()));
            StringUtils.sendMessage(sender, ClanUtils.getMembersFormatted(clan.get()));
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}
