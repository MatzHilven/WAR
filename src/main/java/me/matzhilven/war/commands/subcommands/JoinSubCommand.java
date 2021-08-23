package me.matzhilven.war.commands.subcommands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class JoinSubCommand implements SubCommand {

    private final WARPlugin main;

    public JoinSubCommand(WARPlugin main) {
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

        if (!main.getClanManager().getClanByName(args[1]).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-clan"));
            return;
        }

        Clan clan = main.getClanManager().getClanByName(args[1]).get();

        if (!clan.getInvited().stream().map(UUID::toString).collect(Collectors.toList()).contains(sender.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-invited"));
            return;
        }

        clan.addMember(sender);

        clan.getInvited().remove(sender.getUniqueId());
    }

    @Override
    public String getPermission() {
        return null;
    }
}
