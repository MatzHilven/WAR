package me.matzhilven.war.war.spawns;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.ConfigUtils;
import me.matzhilven.war.utils.Logger;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnManager {

    private final WARPlugin main;
    private final Set<Spawn> spawns;

    public SpawnManager(WARPlugin main) {
        this.main = main;
        this.spawns = new HashSet<>();

        Logger.log("Loading spawns...");
        loadSpawns();
    }

    private void loadSpawns() {
        if (main.getSpawns().get("spawns") == null) return;

        main.getSpawns().getConfigurationSection("spawns").getKeys(false).forEach(spawn -> {
            Spawn s = new Spawn(
                    main.getSpawns().getString("spawns." + spawn + ".name"),
                    main.getSpawns().getStringList("spawns." + spawn + ".lore"),
                    ConfigUtils.toLocation(main.getSpawns().getString("spawns." + spawn + ".location")),
                    Material.matchMaterial(main.getSpawns().getString("spawns." + spawn + ".icon")),
                    main.getSpawns().getInt("spawns." + spawn + ".slot"));

            Logger.log(" Loaded spawn " + s.getNameUncolorized());
            spawns.add(s);
        });
    }

    public Optional<Spawn> byName(String name) {
        return spawns.stream().filter(spawn -> spawn.getNameUncolorized().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Spawn> bySlot(int slot) {
        return spawns.stream().filter(spawn -> spawn.getSlot() == slot).findFirst();
    }

    public void addSpawn(Spawn s) {
        spawns.add(s);

        main.getSpawns().set("spawns." + s.getNameUncolorized() + ".name", s.getName());
        main.getSpawns().set("spawns." + s.getNameUncolorized() + ".lore", s.getLore());
        main.getSpawns().set("spawns." + s.getNameUncolorized() + ".location", ConfigUtils.toString(s.getLocation()));
        main.getSpawns().set("spawns." + s.getNameUncolorized() + ".icon", s.getIcon().toString());
        main.saveSpawns();
    }

    public void removeSpawn(Spawn s) {
        spawns.remove(s);
        main.getSpawns().set("spawns." + s.getNameUncolorized(), null);
        main.saveSpawns();
    }

    public Set<Spawn> getSpawns() {
        return spawns;
    }

    public Location getRandom() {
        if (spawns.size() == 0) return null;
        if (spawns.size() == 1) return new ArrayList<>(spawns).get(0).getLocation();
        return new ArrayList<>(spawns).get(ThreadLocalRandom.current().nextInt(spawns.size() - 1)).getLocation();
    }
}
