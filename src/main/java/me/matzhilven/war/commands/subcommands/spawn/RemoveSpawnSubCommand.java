package me.matzhilven.war.commands.subcommands.spawn;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.spawns.Spawn;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Optional;

public class RemoveSpawnSubCommand implements SubCommand {

    private final WARPlugin main;

    public RemoveSpawnSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        if (args.length != 2) {
            StringUtils.sendMessage(sender, main.getMessages().getStringList("usage-spawns"));
            return;
        }

        Optional<Spawn> spawn = main.getSpawnManager().byName(args[1]);

        if (!spawn.isPresent()) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-spawn"));
            return;
        }

        main.getSpawnManager().removeSpawn(spawn.get());
        StringUtils.sendMessage(sender, main.getMessages().getString("removed-spawn")
                .replace("%spawn%", spawn.get().getName()));
    }

    @Override
    public String getPermission() {
        return "war.admin";
    }
}
