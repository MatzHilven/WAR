package me.matzhilven.war.commands.subcommands.spawn;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.spawns.Spawn;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class AddSpawnSubCommand implements SubCommand {

    private final WARPlugin main;

    public AddSpawnSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-spawns"));
            return;
        }

        main.getSpawnManager().addSpawn(new Spawn(args[1], sender.getLocation()));
        StringUtils.sendMessage(sender, main.getMessages().getString("added-spawn")
                .replace("%spawn%", args[1]));
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
