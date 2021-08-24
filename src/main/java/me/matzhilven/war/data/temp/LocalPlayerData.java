package me.matzhilven.war.data.temp;

import me.matzhilven.war.clan.Clan;

public class LocalPlayerData implements TempPlayerData {

    private String UUID;
    private Clan clan;

    private int kills;
    private int deaths;
    private int currentKillStreak;
    private int bestKillStreak;

    @Override
    public String getUUID() {
        return UUID;
    }
    @Override
    public void setUUID(String uuid) {
        this.UUID = uuid;
    }

    @Override
    public Clan getClan() {
        return this.clan;
    }

    @Override
    public void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public int getKills() {
        return this.kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public int getDeaths() {
        return this.deaths;
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public String getKD() {
        if (deaths == 0) {
            return String.format("%.2f", (float) kills);
        }
        return String.format("%.2f", (float) kills / deaths);
    }

    @Override
    public int getCurrentKillStreak() {
        return this.currentKillStreak;
    }

    @Override
    public void setCurrentKillStreak(int killStreak) {
        this.currentKillStreak = killStreak;
    }

    @Override
    public int getBestKillStreak() {
        return this.bestKillStreak;
    }

    @Override
    public void setBestKillStreak(int killStreak) {
        this.bestKillStreak = killStreak;
    }

    public void addKill() {
        setKills(getKills() + 1);
        setCurrentKillStreak(getCurrentKillStreak() + 1);
        if (getCurrentKillStreak() > getBestKillStreak()) setBestKillStreak(getCurrentKillStreak());
    }

    public void addDeath() {
        setDeaths(getDeaths() + 1);
        setCurrentKillStreak(0);
    }
}
