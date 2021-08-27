package me.matzhilven.war.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.Logger;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClanManager {

    private final WARPlugin main;

    private Set<Clan> clans;

    public ClanManager(WARPlugin main) {
        this.main = main;
        this.clans = new HashSet<>();

        Logger.log("Loading clans...");
        clans = main.getDb().loadClans();
    }

    public boolean addClan(Clan clan) {
        if (clans.contains(clan) || checkName(clan.getName())) return false;
        clans.add(clan);
        main.getDb().saveClan(clan);
        return true;
    }

    public boolean removeClan(Clan clan) {
        if (!clans.contains(clan)) return false;
        clans.remove(clan);
        main.getDb().removeClan(clan);
        return true;
    }

    public Set<Clan> getClans() {
        return clans;
    }

    public Optional<Clan> getClan(Player player) {
        return getClan(player.getUniqueId());
    }

    public Optional<Clan> getClan(UUID uuid) {
        return clans.stream().filter(clan ->
                uuid.toString().equals(clan.getLeader().toString()) ||
                        clan.getCoLeaders().stream().map(UUID::toString).collect(Collectors.toList())
                                .contains(uuid.toString()) ||
                        clan.getMembers().stream().map(UUID::toString).collect(Collectors.toList())
                                .contains(uuid.toString())).findFirst();
    }

    public Optional<Clan> getOwnedClan(Player player) {
        return clans.stream().filter(clan -> player.getUniqueId().toString().equals(clan.getLeader().toString())).findFirst();
    }

    public Optional<Clan> getCoOwnedClan(Player player) {
        return clans.stream().filter(clan -> clan.getCoLeaders().stream().map(UUID::toString).collect(Collectors.toList())
                .contains(player.getUniqueId().toString())).findFirst();
    }

    public Optional<Clan> getClanByName(String name) {
        return clans.stream().filter(clan -> clan.getName().equalsIgnoreCase(name)).findFirst();
    }

    private boolean checkName(String name) {
        for (Clan clan : clans) {
            if (clan.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
