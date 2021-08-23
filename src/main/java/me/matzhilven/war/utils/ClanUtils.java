package me.matzhilven.war.utils;

import me.matzhilven.war.clan.Clan;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class ClanUtils {

    public static String getMembersFormatted(Clan clan) {
        List<UUID> members = clan.getAll();

        StringBuilder result = new StringBuilder();
        for (UUID member : members) {
            OfflinePlayer player = Bukkit.getPlayer(member);
            if (player == null) continue;
            result.append(player.isOnline() ? "&a" + player.getName() : "&c" + player.getName());
            result.append("&7,");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }
}
