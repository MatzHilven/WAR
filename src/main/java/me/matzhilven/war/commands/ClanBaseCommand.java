package me.matzhilven.war.commands;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.StringUtils;
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
            StringUtils.sendMessage(sender, "invalid-permissions");
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
                cmds.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                return StringUtil.copyPartialMatches(args[1], cmds, new ArrayList<>());

        }

        return null;
    }
}
