package me.matzhilven.war.commands.subcommands.kits;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.kit.ArmorstandKit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SetKitLocationSubCommand implements SubCommand {

    private final WARPlugin main;

    public SetKitLocationSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-kits"));
            return;
        }

        if (!main.getKitManager().byName(args[1]).isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-kit"));
            return;
        }

        ArmorstandKit kit = main.getKitManager().byName(args[1]).get();
        kit.setSpawnLocation(sender.getLocation(), true);
        StringUtils.sendMessage(sender, main.getMessages().getString("set-location")
                .replace("%kit%", kit.getName())
        );

    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
