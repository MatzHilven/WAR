package me.matzhilven.war.sqlite;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.utils.StringUtils;

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

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clans WHERE name=?");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

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
        if (!exists(clan.getName())) {
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
        if (!exists(clan.getName())) return;
        try {
            PreparedStatement ps = getSQLConnection().prepareStatement("DELETE FROM clans WHERE name=?");
            ps.setString(1, clan.getName());

            ps.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
        }
    }

    public boolean exists(String name) {
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
