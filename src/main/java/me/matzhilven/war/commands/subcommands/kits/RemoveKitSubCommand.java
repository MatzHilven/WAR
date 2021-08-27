package me.matzhilven.war.commands.subcommands.kits;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.kit.ArmorstandKit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Optional;

public class RemoveKitSubCommand implements SubCommand {

    private final WARPlugin main;

    public RemoveKitSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-kits"));
            return;
        }

        Optional<ArmorstandKit> kit = main.getKitManager().byName(args[1]);

        if (!kit.isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-kit"));
            return;
        }

        main.getKitManager().removeKit(kit.get());
        StringUtils.sendMessage(sender, main.getMessages().getString("removed-kit")
                .replace("%kit%", kit.get().getName()));
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
