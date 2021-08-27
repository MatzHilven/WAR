package me.matzhilven.war.commands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.kit.ArmorstandKit;
import me.matzhilven.war.war.spawns.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClanBaseCommand implements CommandExecutor, TabExecutor {

    private final WARPlugin main;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public ClanBaseCommand(WARPlugin main) {
        this.main = main;
        main.getCommand("clan").setExecutor(this);
        main.getCommand("clan").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            subCommands.get("info").onCommand(p, command, args);
            return true;
        }

        String subCommandString = args[0];

        if (!subCommands.containsKey(subCommandString)) {
            StringUtils.sendMessage(sender, main.getMessages().getString("usage"));
            return true;
        }

        if (subCommands.get(args[0]).getPermission() != null &&
                !sender.hasPermission(subCommands.get(args[0]).getPermission())) {
            StringUtils.sendMessage(sender, main.getMessages().getString("invalid-permissions"));
            return true;
        }

        subCommands.get(args[0]).onCommand(p, command, args);

        return true;
    }

    public void registerSubCommand(String cmd, SubCommand subCommand) {
        subCommands.put(cmd, subCommand);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> cmds = new ArrayList<>();

        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0],
                        subCommands.keySet()
                        , new ArrayList<>());
            case 2:
                if (args[0].equalsIgnoreCase("war")) {
                    return StringUtil.copyPartialMatches(args[1],
                            main.getClanManager().getClans().stream().map(Clan::getName).collect(Collectors.toList()),
                            new ArrayList<>());
                } else if (args[0].equalsIgnoreCase("setspawnlocation") || args[0].equalsIgnoreCase("removekit")) {
                    return StringUtil.copyPartialMatches(args[1],
                            main.getKitManager().getKits().stream().map(ArmorstandKit::getNameUncolorized).collect(Collectors.toList()),
                            new ArrayList<>());
                } else if (args[0].equalsIgnoreCase("removespawn")) {
                    return StringUtil.copyPartialMatches(args[1],
                            main.getSpawnManager().getSpawns().stream().map(Spawn::getNameUncolorized).collect(Collectors.toList()),
                            new ArrayList<>());
                }
                cmds.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                return StringUtil.copyPartialMatches(args[1], cmds, new ArrayList<>());
            case 3:
                if (args[0].equalsIgnoreCase("war")) {
                    return StringUtil.copyPartialMatches(args[2],
                            main.getClanManager().getClans().stream().map(Clan::getName).collect(Collectors.toList()),
                            new ArrayList<>());
                }
            case 4:
                if (args[0].equalsIgnoreCase("war")) {
                    cmds.add("5");
                    cmds.add("10");
                    cmds.add("15");
                    cmds.add("20");
                    return StringUtil.copyPartialMatches(args[3], cmds, new ArrayList<>());
                }
        }

        return null;
    }
}
