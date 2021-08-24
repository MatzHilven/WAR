package me.matzhilven.war;

import me.matzhilven.war.clan.ClanManager;
import me.matzhilven.war.commands.ClanBaseCommand;
import me.matzhilven.war.commands.subcommands.StatsCommand;
import me.matzhilven.war.commands.subcommands.clan.*;
import me.matzhilven.war.commands.subcommands.war.EndWarSubCommand;
import me.matzhilven.war.commands.subcommands.war.WarInfoSubCommand;
import me.matzhilven.war.commands.subcommands.war.WarSubCommand;
import me.matzhilven.war.data.sqlite.Database;
import me.matzhilven.war.data.sqlite.SQLite;
import me.matzhilven.war.listeners.PlayerListeners;
import me.matzhilven.war.war.War;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class WARPlugin extends JavaPlugin {

    private FileConfiguration messages;
    private ClanManager clanManager;
    private Database db;

    private War currentWar;

    @Override
    public void onEnable() {
        createFiles();

        db = new SQLite(this, "data");
        db.load();

        clanManager = new ClanManager(this);

        registerCommands();

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.getDb().saveClans();
            this.getDb().savePlayers();
        }, 20L * 60L * 5L, 20L * 60L * 5L);
    }

    @Override
    public void onDisable() {
        db.saveClans();
        db.savePlayers();
    }

    private void createFiles() {
        saveDefaultConfig();

        File messagesF = new File(getDataFolder(), "messages.yml");

        if (!messagesF.exists()) {
            messagesF.getParentFile().mkdir();
            saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();

        try {
            messages.load(messagesF);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        ClanBaseCommand cmd = new ClanBaseCommand(this);
        cmd.registerSubCommand("cancelinvite", new CancelInviteSubCommand(this));
        cmd.registerSubCommand("chat", new ChatSubCommand(this));
        cmd.registerSubCommand("create", new CreateSubCommand(this));
        cmd.registerSubCommand("demote", new DemoteSubCommand(this));
        cmd.registerSubCommand("disband", new DisbandSubCommand(this));
        cmd.registerSubCommand("help", new HelpSubCommand(this));
        cmd.registerSubCommand("info", new InfoSubCommand(this));
        cmd.registerSubCommand("invite", new InviteSubCommand(this));
        cmd.registerSubCommand("join", new JoinSubCommand(this));
        cmd.registerSubCommand("kick", new KickSubCommand(this));
        cmd.registerSubCommand("leave", new LeaveSubCommand(this));
        cmd.registerSubCommand("list", new ListSubCommand(this));
        cmd.registerSubCommand("promote", new PromoteSubCommand(this));
        cmd.registerSubCommand("stats", new StatsSubCommand(this));

        cmd.registerSubCommand("war", new WarSubCommand(this));
        cmd.registerSubCommand("warinfo", new WarInfoSubCommand(this));
        cmd.registerSubCommand("endwar", new EndWarSubCommand(this));

        new StatsCommand(this);
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public Database getDb() {
        return db;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public void setCurrentWar(War currentWar) {
        this.currentWar = currentWar;
    }

    public War getCurrentWar() {
        return currentWar;
    }
}
