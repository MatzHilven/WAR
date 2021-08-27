package me.matzhilven.war.commands.subcommands.kits;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.kit.ArmorstandKit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CreateKitSubCommand implements SubCommand {

    private final WARPlugin main;

    public CreateKitSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-kits"));
            return;
        }

        ArmorstandKit armorstandKit = new ArmorstandKit(
                args[1],
                sender.getInventory().getItemInMainHand(),
                sender.getInventory().getHelmet(),
                sender.getInventory().getChestplate(),
                sender.getInventory().getLeggings(),
                sender.getInventory().getBoots(),
                sender.getInventory().getContents()
        );

        main.getKitManager().addKit(armorstandKit);

        StringUtils.sendMessage(sender, main.getMessages().getString("added-kit")
                .replace("%kit%", args[1]));
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
