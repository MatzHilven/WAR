package me.matzhilven.war.data.sqlite;

import me.matzhilven.war.WARPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {

    private final String dbname;
    private final WARPlugin instance;

    public SQLite(WARPlugin instance, String dbname) {
        super(instance);
        this.instance = instance;
        this.dbname = dbname;
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(instance.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                instance.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            instance.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            instance.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS clans (" +
                    "`name` varchar(100) NOT NULL," +
                    "`leader` varchar(32) NOT NULL," +
                    "`coLeaders` text(1000) NOT NULL," +
                    "`members` text(1000) NOT NULL," +
                    "`wins` int(255) NOT NULL," +
                    "`losses` int(255) NOT NULL," +
                    "`kills` int(255) NOT NULL," +
                    "PRIMARY KEY (`name`)" +
                    ");";
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement s = connection.createStatement();
            String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS players (" +
                    "`uuid` varchar(32) NOT NULL," +
                    "`wins` int(255) NOT NULL," +
                    "`losses` int(255) NOT NULL," +
                    "`kills` int(255) NOT NULL," +
                    "`deaths` int(255) NOT NULL," +
                    "`killstreak` int(255) NOT NULL," +
                    "PRIMARY KEY (`uuid`)" +
                    ");";
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
