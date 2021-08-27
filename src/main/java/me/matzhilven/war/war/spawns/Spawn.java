package me.matzhilven.war.war.spawns;

import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class Spawn {

    private final String name;
    private final List<String> lore;
    private final Location location;
    private final Material icon;
    private final int slot;

    public Spawn(String name, Location location) {
        this(name, Arrays.asList(" ", "&7Default lore"), location, Material.STONE, 0);
    }

    public Spawn(String name, List<String> lore, Location location, Material icon, int slot) {
        this.name = name;
        this.lore = lore;
        this.location = location;
        this.icon = icon;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public String getNameUncolorized() {
        return StringUtils.removeColor(name);
    }

    public List<String> getLore() {
        return lore;
    }

    public Location getLocation() {
        return location;
    }

    public Material getIcon() {
        return icon;
    }

    public int getSlot() {
        return slot;
    }
}
