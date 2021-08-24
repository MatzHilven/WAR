package me.matzhilven.war.clan;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan {

    private final WARPlugin main = WARPlugin.getPlugin(WARPlugin.class);

    private String name;

    private UUID leader;
    private List<UUID> coLeaders;
    private List<UUID> members;

    private List<UUID> invited;
    private List<UUID> inChat;

    private int wins;
    private int losses;
    private int kills;

    public Clan(String name, UUID leader) {
        this(name, leader, new ArrayList<>(), new ArrayList<>(), 0, 0, 0);
    }

    public Clan(String name, UUID leader, List<UUID> coLeaders, List<UUID> members, int wins, int losses, int kills) {
        this.name = name;
        this.leader = leader;
        this.coLeaders = coLeaders;
        this.members = members;
        this.invited = new ArrayList<>();
        this.inChat = new ArrayList<>();
        this.wins = wins;
        this.losses = losses;
        this.kills = kills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public List<UUID> getCoLeaders() {
        return coLeaders;
    }

    public void setCoLeaders(List<UUID> coLeaders) {
        this.coLeaders = coLeaders;
    }

    public void addCoLeader(UUID uuid) {
        coLeaders.add(uuid);
        members.remove(uuid);
    }

    public void removeCoLeader(UUID uuid) {
        coLeaders.remove(uuid);
        members.add(uuid);
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public void addMember(Player member) {
        members.add(member.getUniqueId());
        getOnline().forEach(player -> StringUtils.sendMessage(player, main.getMessages().getString("joined")
                .replace("%player%", member.getName())
        ));
    }

    public void removeMember(Player member, boolean kick) {
        removeMember(member.getUniqueId(), kick);
    }

    public void removeMember(UUID uuid, boolean kick) {
        coLeaders.remove(uuid);
        members.remove(uuid);
        if (!kick) {
            getOnline().forEach(player -> StringUtils.sendMessage(player, main.getMessages().getString("left")
                    .replace("%player%", Bukkit.getOfflinePlayer(uuid).getName())
            ));
        }
    }

    public List<UUID> getInvited() {
        return invited;
    }

    public List<UUID> getInChat() {
        return inChat;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public List<Player> getOnline() {
        List<Player> online = new ArrayList<>();
        for (UUID uuid : getAll()) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) online.add(Bukkit.getPlayer(uuid));
        }
        return online;
    }

    public List<UUID> getAll() {
        List<UUID> members = new ArrayList<>();
        members.add(leader);
        members.addAll(coLeaders);
        members.addAll(this.members);

        return members;
    }

    @Override
    public String toString() {
        return "Clan{" +
                "name='" + name + '\'' +
                ", leader=" + leader +
                ", coLeaders=" + coLeaders +
                ", members=" + members +
                ", wins=" + wins +
                ", losses=" + losses +
                ", kills=" + kills +
                '}';
    }
}
