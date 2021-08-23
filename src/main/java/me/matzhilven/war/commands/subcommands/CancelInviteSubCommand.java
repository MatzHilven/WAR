package me.matzhilven.war.commands.subcommands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class CancelInviteSubCommand implements SubCommand {

    private final WARPlugin main;

    public CancelInviteSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage"));
            return;
        }

        if (!main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
            return;
        }

        if (!main.getClanManager().getOwnedClan(sender).isPresent() && !main.getClanManager().getCoOwnedClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("no-clan-owned"));
            return;
        }

        if (Bukkit.getPlayer(args[1]) == null) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-player"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        Clan clan = main.getClanManager().getOwnedClan(sender).get();

        if (!clan.getInvited().stream().map(UUID::toString).collect(Collectors.toList()).contains(target.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-invited"));
            return;
        }

        clan.getInvited().remove(target.getUniqueId());

        StringUtils.sendMessage(sender, main.getMessages().getString("removed-invited")
        .replace("%player%", target.getName()));

    }

    @Override
    public String getPermission() {
        return null;
    }
}
