package me.matzhilven.war.data.sqlite;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.data.sqlite.player.PlayerData;
import me.matzhilven.war.data.sqlite.player.SQLitePlayerData;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public abstract class Database {

    public Connection connection;
    private WARPlugin main;

    public Database(WARPlugin instance) {
        main = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    //  Clan Data
    public Set<Clan> loadClans() {
        Set<Clan> clans = new HashSet<>();

        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM clans");
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                Clan clan = new Clan(
                        results.getString("name"),
                        UUID.fromString(results.getString("leader")),
                        StringUtils.toUUIDList(results.getString("coLeaders")),
                        StringUtils.toUUIDList(results.getString("members")),
                        results.getInt("wins"),
                        results.getInt("losses"),
                        results.getInt("kills")
                );

                clans.add(clan);
            }

            return clans;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new HashSet<>();
    }

    public void saveClan(Clan clan) {
        if (!existsClan(clan.getName())) {
            addClan(clan);
            return;
        }

        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("UPDATE clans SET " +
                    "name=?,leader=?,coLeaders=?,members=?,wins=?,losses=?," +
                    "kills=? " +
                    "WHERE name=?");
            ps.setString(1, clan.getName());
            ps.setString(2, clan.getLeader().toString());
            ps.setString(3, StringUtils.to1String(clan.getCoLeaders()));
            ps.setString(4, StringUtils.to1String(clan.getMembers()));
            ps.setInt(5, clan.getWins());
            ps.setInt(6, clan.getLosses());
            ps.setInt(7, clan.getKills());
            ps.setString(8, clan.getName());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    private void addClan(Clan clan) {
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("INSERT INTO clans " +
                    "(name,leader,coLeaders,members,wins,losses,kills) " +
                    "VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, clan.getName());
            ps.setString(2, clan.getLeader().toString());
            ps.setString(3, StringUtils.to1String(clan.getCoLeaders()));
            ps.setString(4, StringUtils.to1String(clan.getMembers()));
            ps.setInt(5, clan.getWins());
            ps.setInt(6, clan.getLosses());
            ps.setInt(7, clan.getKills());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    public void saveClans() {
        main.getClanManager().getClans().forEach(this::saveClan);
    }

    public void removeClan(Clan clan) {
        if (!existsClan(clan.getName())) return;
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("DELETE FROM clans WHERE name=?");
            ps.setString(1, clan.getName());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    public boolean existsClan(String name) {
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM clans WHERE name=?");
            ps.setString(1, name);

            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Player Data
    public void addPlayer(Player player) {
        SQLitePlayerData playerData = new SQLitePlayerData();
        playerData.setUUID(player.getUniqueId().toString());
        if (!(existsPlayer(player.getUniqueId()))) {
            try {
                PreparedStatement ps = getSQLConnection().prepareStatement("INSERT INTO players " +
                        "(uuid,wins,losses,kills,deaths,killstreak) " +
                        "VALUES (?,?,?,?,?,?)");

                ps.setString(1, player.getUniqueId().toString());
                ps.setInt(2, playerData.getWins());
                ps.setInt(3, playerData.getLosses());
                ps.setInt(4, playerData.getKills());
                ps.setInt(5, playerData.getDeaths());
                ps.setInt(6, playerData.getBestKillStreak());

                ps.executeUpdate();
            } catch (SQLException ex) {
                main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
            }
        } else {
            try {
                PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM players WHERE uuid=?");
                ps.setString(1, player.getUniqueId().toString());

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("uuid").equalsIgnoreCase(player.getUniqueId().toString())) {
                        playerData.setWins(rs.getInt("wins"));
                        playerData.setLosses(rs.getInt("losses"));
                        playerData.setKills(rs.getInt("kills"));
                        playerData.setDeaths(rs.getInt("deaths"));
                        playerData.setBestKillStreak(rs.getInt("killstreak"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        PlayerData.get().put(player.getUniqueId(), playerData);
    }

    public void savePlayer(Player player) {
        SQLitePlayerData playerData = PlayerData.get(player.getUniqueId());

        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("UPDATE players SET " +
                    "uuid=?,wins=?,losses=?,kills=?,deaths=?,killstreak=? " +
                    "WHERE uuid=?");
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, playerData.getWins());
            ps.setInt(3, playerData.getLosses());
            ps.setInt(4, playerData.getKills());
            ps.setInt(5, playerData.getDeaths());
            ps.setInt(6, playerData.getBestKillStreak());
            ps.setString(7, player.getUniqueId().toString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    public void savePlayers() {
        main.getServer().getOnlinePlayers().forEach(this::savePlayer);
    }

    public void removePlayer(Player player) {
        if (!existsPlayer(player.getUniqueId())) return;
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("DELETE FROM players WHERE uuid=?");
            ps.setString(1, player.getUniqueId().toString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    public boolean existsPlayer(UUID uuid) {
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("SELECT * FROM players WHERE uuid=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
        }
    }

}
