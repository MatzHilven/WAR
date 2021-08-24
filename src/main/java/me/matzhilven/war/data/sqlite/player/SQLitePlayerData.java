package me.matzhilven.war.data.sqlite.player;

public class SQLitePlayerData implements PlayerData {

    private String UUID;

    private int wins;
    private int losses;
    private int kills;
    private int deaths;
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
    public int getWins() {
        return this.wins;
    }

    @Override
    public void setWins(int wins) {
        this.wins = wins;
    }

    @Override
    public int getLosses() {
        return this.losses;
    }

    @Override
    public void setLosses(int losses) {
        this.losses = losses;
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
    public int getBestKillStreak() {
        return this.bestKillStreak;
    }

    @Override
    public void setBestKillStreak(int killStreak) {
        this.bestKillStreak = killStreak;
    }

    @Override
    public String getKD() {
        if (deaths == 0) {
            return String.format("%.2f", (float) kills);
        }
        return String.format("%.2f", (float) kills / deaths);
    }

    @Override
    public String toString() {
        return "SQLitePlayerData{" +
                "wins=" + wins +
                ", losses=" + losses +
                ", kills=" + kills +
                ", deaths=" + deaths +
                ", bestKillStreak=" + bestKillStreak +
                '}';
    }
}
