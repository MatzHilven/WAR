package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;


public class InviteSubCommand implements SubCommand {

    private final WARPlugin main;

    public InviteSubCommand(WARPlugin main) {
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

        Player target = Bukkit.getPlayer(args[1]);

        if (target == sender) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invite-self"));
            return;
        }

        if (main.getClanManager().getClan(target).isPresent()) {
            Clan clan = main.getClanManager().getClan(target).get();
            if (clan == main.getClanManager().getClan(sender).get()) {
                StringUtils.sendMessage(sender, main.getMessages().getString("invite-member"));
            } else {
                StringUtils.sendMessage(sender, main.getMessages().getString("invite-in-clan"));
            }
            return;
        }

        Clan clan = main.getClanManager().getOwnedClan(sender).get();

        if (clan.getInvited().stream().map(UUID::toString).collect(Collectors.toList()).contains(target.getUniqueId().toString())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("already-invited"));
            return;
        }

        StringUtils.sendMessage(sender, main.getMessages().getString("invite").replace("%player%", target.getName()));
        sendClickableCommand(target, main.getMessages().getString("invite-target")
                .replace("%clan%", clan.getName())
                , "/c join " + clan.getName());

        clan.getInvited().add(target.getUniqueId());
    }

    @Override
    public String getPermission() {
        return null;
    }

    public void sendClickableCommand(Player player, String message, String command) {
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(StringUtils.colorize(message)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        player.spigot().sendMessage(component);
    }
}
