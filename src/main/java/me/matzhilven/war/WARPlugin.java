package me.matzhilven.war;

import me.matzhilven.war.clan.ClanManager;
import me.matzhilven.war.commands.ClanBaseCommand;
import me.matzhilven.war.commands.StatsCommand;
import me.matzhilven.war.commands.subcommands.clan.*;
import me.matzhilven.war.commands.subcommands.kits.CreateKitSubCommand;
import me.matzhilven.war.commands.subcommands.kits.ListKitsSubCommand;
import me.matzhilven.war.commands.subcommands.kits.RemoveKitSubCommand;
import me.matzhilven.war.commands.subcommands.kits.SetKitLocationSubCommand;
import me.matzhilven.war.commands.subcommands.spawn.AddSpawnSubCommand;
import me.matzhilven.war.commands.subcommands.spawn.ListSpawnsSubCommand;
import me.matzhilven.war.commands.subcommands.spawn.RemoveSpawnSubCommand;
import me.matzhilven.war.commands.subcommands.war.EndWarSubCommand;
import me.matzhilven.war.commands.subcommands.war.WarInfoSubCommand;
import me.matzhilven.war.commands.subcommands.war.WarSubCommand;
import me.matzhilven.war.data.Database;
import me.matzhilven.war.data.mysql.MySQL;
import me.matzhilven.war.data.sqlite.SQLite;
import me.matzhilven.war.listeners.InventoryListeners;
import me.matzhilven.war.listeners.PlayerListeners;
import me.matzhilven.war.utils.Logger;
import me.matzhilven.war.war.War;
import me.matzhilven.war.war.kit.KitManager;
import me.matzhilven.war.war.spawns.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class WARPlugin extends JavaPlugin {

    private FileConfiguration messages;
    private FileConfiguration kits;
    private FileConfiguration spawns;

    private ClanManager clanManager;
    private KitManager kitManager;
    private SpawnManager spawnManager;

    private Database db;

    private War currentWar;

    @Override
    public void onEnable() {
        createFiles();

        if (getConfig().getBoolean("mysql.enabled")) {
            Logger.log("Loading database... (Using MySQL)");
            db = new MySQL(this);
            db.load();
        } else {
            Logger.log("Loading database... (Using SQLite)");
            db = new SQLite(this, "data");
            db.load();
        }

        clanManager = new ClanManager(this);
        kitManager = new KitManager(this);
        spawnManager = new SpawnManager(this);

        Logger.log("Registering commands...");
        registerCommands();

        Logger.log("Registering listeners...");
        new PlayerListeners(this);
        new InventoryListeners(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.getDb().saveClans();
            this.getDb().savePlayers();
        }, 20L * 60L * 5L, 20L * 60L * 5L);
    }

    @Override
    public void onDisable() {
        db.saveClans();
        db.savePlayers();

        if (currentWar != null) {
            currentWar.end();
            Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()));
        }
    }

    private void registerCommands() {
        ClanBaseCommand cmd = new ClanBaseCommand(this);
        cmd.registerSubCommand("cancelinvite", new CancelInviteSubCommand(this));
        cmd.registerSubCommand("chat", new ChatSubCommand(this));
        cmd.registerSubCommand("c", new ChatSubCommand(this));
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

        cmd.registerSubCommand("kits", new ListKitsSubCommand(this));
        cmd.registerSubCommand("createkit", new CreateKitSubCommand(this));
        cmd.registerSubCommand("removekit", new RemoveKitSubCommand(this));
        cmd.registerSubCommand("setspawnlocation", new SetKitLocationSubCommand(this));

        cmd.registerSubCommand("addspawn", new AddSpawnSubCommand(this));
        cmd.registerSubCommand("removespawn", new RemoveSpawnSubCommand(this));
        cmd.registerSubCommand("spawns", new ListSpawnsSubCommand(this));

        new StatsCommand(this);
    }

    private void createFiles() {
        saveDefaultConfig();

        File messagesF = new File(getDataFolder(), "messages.yml");
        File kitsF = new File(getDataFolder(), "kits.yml");
        File spawnsF = new File(getDataFolder(), "spawns.yml");

        if (!messagesF.exists()) {
            messagesF.getParentFile().mkdir();
            saveResource("messages.yml", false);
        }

        if (!kitsF.exists()) {
            kitsF.getParentFile().mkdir();
            saveResource("kits.yml", false);
        }

        if (!spawnsF.exists()) {
            spawnsF.getParentFile().mkdir();
            saveResource("spawns.yml", false);
        }

        messages = new YamlConfiguration();
        kits = new YamlConfiguration();
        spawns = new YamlConfiguration();

        try {
            messages.load(messagesF);
            kits.load(kitsF);
            spawns.load(spawnsF);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKits() {
        File kitsF = new File(getDataFolder(), "kits.yml");

        try {
            kits.save(kitsF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSpawns() {
        File spawnsF = new File(getDataFolder(), "spawns.yml");

        try {
            spawns.save(spawnsF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public Database getDb() {
        return db;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getKits() {
        return kits;
    }

    public FileConfiguration getSpawns() {
        return spawns;
    }

    public War getCurrentWar() {
        return currentWar;
    }

    public void setCurrentWar(War currentWar) {
        this.currentWar = currentWar;
    }
}
