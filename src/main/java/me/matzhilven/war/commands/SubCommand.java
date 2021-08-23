package me.matzhilven.war.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommand {
    void onCommand(Player sender, Command command, String[] args);

    String getPermission();
}
