package me.matzhilven.war.commands.subcommands.clan;

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

public class KickSubCommand implements SubCommand {

    private final WARPlugin main;

    public KickSubCommand(WARPlugin main) {
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

        if (Bukkit.getOfflinePlayer(args[1]) == null) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-player"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (target == sender) {
            StringUtils.sendMessage(sender, main.getMessages().getString("kick-self"));
            return;
        }

        if (!main.getClanManager().getClan(target.getUniqueId()).isPresent() ||
                main.getClanManager().getClan(target.getUniqueId()).get() != main.getClanManager().getClan(sender).get()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("kick-not-in-clan"));
            return;
        }

        Clan clan = main.getClanManager().getClan(sender).get();

        if (target.getUniqueId().toString().equals(clan.getLeader().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("kick-leader"));
            return;
        }

        if (isCo(sender, clan) && isCo(target.getUniqueId(), clan)) {
            StringUtils.sendMessage(sender, main.getMessages().getString("kick-co"));
            return;
        }

        clan.removeMember(target.getUniqueId(), true);

        StringUtils.sendMessage(sender, main.getMessages().getString("kick")
        .replace("%player%", target.getName()));
        if (target.isOnline()) {
            StringUtils.sendMessage(target.getPlayer(), main.getMessages().getString("kick-target"));
        }

    }

    @Override
    public String getPermission() {
        return null;
    }

    private boolean isCo(Player player, Clan clan) {
        return isCo(player.getUniqueId(), clan);
    }

    private boolean isCo(UUID uuid, Clan clan) {
        return clan.getCoLeaders().stream().map(UUID::toString).collect(Collectors.toList()).contains(uuid.toString());
    }

}
