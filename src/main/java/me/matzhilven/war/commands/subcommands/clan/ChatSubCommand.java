package me.matzhilven.war.commands.subcommands.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ChatSubCommand implements SubCommand {

    private final WARPlugin main;

    public ChatSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (!main.getClanManager().getClan(sender).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("not-in-clan"));
            return;
        }

        Clan clan = main.getClanManager().getClan(sender).get();

        if (clan.getInChat().contains(sender.getUniqueId())) {
            clan.getInChat().remove(sender.getUniqueId());
            StringUtils.sendMessage(sender, main.getMessages().getString("chat-off"));
        } else {
            clan.getInChat().add(sender.getUniqueId());
            StringUtils.sendMessage(sender, main.getMessages().getString("chat-on"));
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}
