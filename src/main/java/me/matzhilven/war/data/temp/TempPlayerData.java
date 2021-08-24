package me.matzhilven.war.data.temp;

import me.matzhilven.war.clan.Clan;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface TempPlayerData {

    Map<UUID, LocalPlayerData> users = new HashMap<>();

    static Map<UUID, LocalPlayerData> get() {
        return users;
    }

    static LocalPlayerData get(UUID uuid) {
        return users.get(uuid);
    }

    String getUUID();

    void setUUID(String uuid);

    Clan getClan();

    void setClan(Clan clan);

    int getKills();

    void setKills(int kills);

    int getDeaths();

    void setDeaths(int deaths);

    String getKD();

    int getCurrentKillStreak();

    void setCurrentKillStreak(int killStreak);

    int getBestKillStreak();

    void setBestKillStreak(int killStreak);
}
