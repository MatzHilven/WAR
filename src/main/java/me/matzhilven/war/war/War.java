package me.matzhilven.war.war;

import dev.jcsoftware.jscoreboards.JPerPlayerMethodBasedScoreboard;
import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.data.sqlite.player.PlayerData;
import me.matzhilven.war.data.sqlite.player.SQLitePlayerData;
import me.matzhilven.war.data.temp.LocalPlayerData;
import me.matzhilven.war.data.temp.TempPlayerData;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class War extends BukkitRunnable {

    private final WARPlugin main = WARPlugin.getPlugin(WARPlugin.class);

    private final List<Player> players;

    private final Clan clan1;
    private final Clan clan2;
    private final int maxKills;
    private JPerPlayerMethodBasedScoreboard scoreboard;
    private int killsClan1;
    private int killsClan2;

    private int deathsClan1;
    private int deathsClan2;

    private int time;

    public War(Clan clan1, Clan clan2, int maxTime) {

        scoreboard = new JPerPlayerMethodBasedScoreboard();

        players = new ArrayList<>();

        this.clan1 = clan1;
        this.clan2 = clan2;
        this.maxKills = main.getConfig().getInt("war.max-kills");
        this.killsClan1 = 0;
        this.killsClan2 = 0;
        this.deathsClan1 = 0;
        this.deathsClan2 = 0;
        this.time = maxTime * 60;

        clan1.getOnline().forEach(player -> {
            LocalPlayerData playerData = new LocalPlayerData();
            playerData.setUUID(player.getUniqueId().toString());
            playerData.setKills(0);
            playerData.setDeaths(0);
            playerData.setCurrentKillStreak(0);
            playerData.setBestKillStreak(0);
            playerData.setClan(clan1);

            players.add(player);

            TempPlayerData.get().put(player.getUniqueId(), playerData);
        });

        clan2.getOnline().forEach(player -> {
            LocalPlayerData playerData = new LocalPlayerData();
            playerData.setUUID(player.getUniqueId().toString());
            playerData.setKills(0);
            playerData.setDeaths(0);
            playerData.setCurrentKillStreak(0);
            playerData.setBestKillStreak(0);
            playerData.setClan(clan2);

            players.add(player);

            TempPlayerData.get().put(player.getUniqueId(), playerData);
        });

        players.forEach(this::setupScoreBoard);
    }

    private void setupScoreBoard(Player player) {
        LocalPlayerData playerData = TempPlayerData.get(player.getUniqueId());

        scoreboard.setTitle(player, StringUtils.colorize(main.getConfig().getString("war.sidebar.title")));

        List<String> lines = new ArrayList<>();
        for (String line : main.getConfig().getStringList("war.sidebar.lines")) {
            line = line.replace("%kills%", StringUtils.format(playerData.getKills()));
            line = line.replace("%deaths%", StringUtils.format(playerData.getDeaths()));
            line = line.replace("%kd%", playerData.getKD());
            line = line.replace("%killstreak%", StringUtils.format(playerData.getCurrentKillStreak()));
            line = line.replace("%best_killstreak%", StringUtils.format(playerData.getBestKillStreak()));
            line = line.replace("%clan_1%", clan1.getName());
            line = line.replace("%clan_2%", clan2.getName());
            line = line.replace("%kills_1%", StringUtils.format(killsClan1));
            line = line.replace("%kills_2%", StringUtils.format(killsClan2));
            line = line.replace("%deaths_2%", StringUtils.format(deathsClan1));
            line = line.replace("%deaths_2%", StringUtils.format(deathsClan2));
            lines.add(StringUtils.colorize(line));
        }

        scoreboard.setLines(player, lines);
        scoreboard.addPlayer(player);
        scoreboard.updateScoreboard();
    }

    public void updateScoreboard(Player player) {
        TempPlayerData playerData = TempPlayerData.get(player.getUniqueId());

        List<String> lines = new ArrayList<>();

        for (String line : main.getConfig().getStringList("war.sidebar.lines")) {
            line = line.replace("%kills%", StringUtils.format(playerData.getKills()));
            line = line.replace("%deaths%", StringUtils.format(playerData.getDeaths()));
            line = line.replace("%kd%", playerData.getKD());
            line = line.replace("%killstreak%", StringUtils.format(playerData.getCurrentKillStreak()));
            line = line.replace("%best_killstreak%", StringUtils.format(playerData.getBestKillStreak()));
            line = line.replace("%clan_1%", clan1.getName());
            line = line.replace("%clan_2%", clan2.getName());
            line = line.replace("%kills_1%", StringUtils.format(killsClan1));
            line = line.replace("%kills_2%", StringUtils.format(killsClan2));
            line = line.replace("%deaths_2%", StringUtils.format(deathsClan1));
            line = line.replace("%deaths_2%", StringUtils.format(deathsClan2));
            lines.add(StringUtils.colorize(line));
        }

        scoreboard.setLines(player, lines);
        scoreboard.updateScoreboard();
    }

    @Override
    public void run() {
        if (--time <= 0) {
            end();
        }
    }

    public boolean isIn(Player player) {
        return players.contains(player);
    }

    public boolean canJoin(Player player) {
        return clan1.getOnline().contains(player) || clan2.getOnline().contains(player);
    }

    public Clan getClan(Player player) {
        return clan1.getOnline().contains(player) ? clan1 : clan2;
    }

    public void addKill(Clan clan) {
        if (clan == clan1) {
            killsClan1++;
            if (killsClan1 >= maxKills) {
                end();
            }
        } else {
            killsClan2++;
            if (killsClan2 >= maxKills) {
                end();
            }
        }
    }

    public void addDeath(Clan clan) {
        if (clan == clan1) {
            deathsClan1++;
        } else {
            deathsClan2++;
        }
    }

    public void addPlayer(Player player) {
        LocalPlayerData playerData = new LocalPlayerData();
        playerData.setUUID(player.getUniqueId().toString());
        playerData.setKills(0);
        playerData.setDeaths(0);
        playerData.setCurrentKillStreak(0);
        playerData.setBestKillStreak(0);
        playerData.setClan(getClan(player));

        TempPlayerData.get().put(player.getUniqueId(), playerData);
        setupScoreBoard(player);
    }

    private void checkWinner() {
        Clan winner = killsClan1 > killsClan2 ? clan1 : clan2;
        Clan loser = winner == clan1 ? clan2 : clan1;

        for (String msg : main.getMessages().getStringList("war-over")) {
            System.out.println(msg);
            msg = msg.replace("%winner%", winner.getName());
            msg = msg.replace("%loser%", loser.getName());
            msg = msg.replace("%clan_1%", clan1.getName());
            msg = msg.replace("%clan_2%", clan2.getName());
            msg = msg.replace("%kills_1%", StringUtils.format(killsClan1));
            msg = msg.replace("%kills_2%", StringUtils.format(killsClan2));
            Bukkit.broadcastMessage(StringUtils.colorize(msg));
        }

        winner.setWins(winner.getWins() + 1);
        loser.setLosses(loser.getLosses() + 1);

        winner.setKills(winner.getKills() + getKills(winner));
        loser.setKills(loser.getKills() + getKills(loser));

        players.forEach(player -> {
            LocalPlayerData localPlayerData = TempPlayerData.get(player.getUniqueId());
            SQLitePlayerData data = PlayerData.get(player.getUniqueId());

            if (winner.getAll().contains(player.getUniqueId())) {
                data.setWins(data.getWins() + 1);
            } else {
                data.setLosses(data.getLosses() + 1);
            }

            data.setKills(data.getKills() + localPlayerData.getKills());
            data.setDeaths(data.getDeaths() + localPlayerData.getDeaths());
            if (data.getBestKillStreak() < localPlayerData.getBestKillStreak()) {
                data.setBestKillStreak(localPlayerData.getBestKillStreak());
            }
        });

    }

    private int getKills(Clan clan) {
        if (clan == clan1) {
            return killsClan1;
        } else {
            return killsClan2;
        }
    }

    public void end() {
        checkWinner();
        players.forEach(player -> {
            if (player.isOnline()) scoreboard.removePlayer(player);
        });
        this.main.setCurrentWar(null);
        this.cancel();
    }

    public Clan getClan1() {
        return clan1;
    }

    public Clan getClan2() {
        return clan2;
    }

    public int getKillsClan1() {
        return killsClan1;
    }

    public int getKillsClan2() {
        return killsClan2;
    }

    public int getDeathsClan1() {
        return deathsClan1;
    }

    public int getDeathsClan2() {
        return deathsClan2;
    }

    public String getTimeFormatted() {
        int minutes = Math.floorDiv(time, 60);
        int seconds = time % 60;

        return minutes + "m " + seconds + "s";
    }
}
