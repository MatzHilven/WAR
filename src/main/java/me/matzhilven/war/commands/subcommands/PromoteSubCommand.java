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

public class PromoteSubCommand implements SubCommand {

    private final WARPlugin main;

    public PromoteSubCommand(WARPlugin main) {
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

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (target == sender) {
            StringUtils.sendMessage(sender, main.getMessages().getString("promote-self"));
            return;
        }

        Clan clan = main.getClanManager().getOwnedClan(sender).get();

        if (!clan.getAll().stream().map(UUID::toString).collect(Collectors.toList()).contains(target.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-your-clan")
                    .replace("%player%", target.getName()));
            return;
        }

        if (clan.getCoLeaders().stream().map(UUID::toString).collect(Collectors.toList()).contains(target.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("already-coleader"));
            return;
        }

        clan.addCoLeader(target.getUniqueId());

        StringUtils.sendMessage(sender, main.getMessages().getString("promoted")
        .replace("%player%", target.getName()));
        if (target.isOnline()) {
            StringUtils.sendMessage(target.getPlayer(), main.getMessages().getString("promoted-target"));
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}
