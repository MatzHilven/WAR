package me.matzhilven.war.commands.subcommands.spawn;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.spawns.Spawn;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListSpawnsSubCommand implements SubCommand {

    private final WARPlugin main;

    public ListSpawnsSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {
        List<String> msg = new ArrayList<>();
        List<Spawn> spawns = new ArrayList<>(main.getSpawnManager().getSpawns());

        for (String s : main.getMessages().getStringList("spawns")) {
            if (s.contains("%spawn")) {
                int i = Integer.parseInt(s.split("%spawn_")[1].substring(0, 1));
                if (i <= spawns.size()) {
                    msg.add(s.replace("%spawn_" + i + "%", spawns.get((i - 1)).getName()));
                }
            } else {
                msg.add(s);
            }
        }

        StringUtils.sendMessage(sender, msg);
    }

    @Override
    public String getPermission() {
        return null;
    }
}
