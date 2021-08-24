package me.matzhilven.war.data.sqlite.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface PlayerData {

    Map<UUID,SQLitePlayerData> users = new HashMap<>();

    static Map<UUID,SQLitePlayerData> get() {
        return users;
    }

    static SQLitePlayerData get(UUID uuid) {
        return users.get(uuid);
    }

    String getUUID();

    void setUUID(String uuid);

    int getWins();

    void setWins(int wins);

    int getLosses();

    void setLosses(int losses);

    int getKills();

    void setKills(int kills);

    int getDeaths();

    void setDeaths(int deaths);

    int getBestKillStreak();

    void setBestKillStreak(int killStreak);

    String getKD();

}
