package me.matzhilven.war.war.kit;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.ConfigUtils;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;

public class ArmorstandKit {

    private final WARPlugin main = WARPlugin.getPlugin(WARPlugin.class);

    private final String name;
    private final ItemStack hand;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack[] contents;
    private Location spawnLocation;

    private ArmorStand armorStand;

    public ArmorstandKit(String name, ItemStack hand, ItemStack helmet, ItemStack chestplate,
                         ItemStack leggings, ItemStack boots, ItemStack[] inv) {
        this.name = name;
        this.hand = hand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.contents = inv;
    }

    public void spawn() {
        if (spawnLocation == null || name == null) return;

        armorStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);

        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(StringUtils.colorize(name));

        armorStand.setArms(true);
        armorStand.setBasePlate(false);

        armorStand.setHelmet(helmet);
        armorStand.setChestplate(chestplate);
        armorStand.setLeggings(leggings);
        armorStand.setBoots(boots);

        armorStand.setItemInHand(hand);

        armorStand.setMetadata("kit", new FixedMetadataValue(main, getNameUncolorized()));
    }

    public void setSpawnLocation(Location spawnLocation, boolean save) {
        this.spawnLocation = spawnLocation;
        if (save) {
            main.getKits().set("kits." + getNameUncolorized().toLowerCase() + ".location", ConfigUtils.toString(spawnLocation));
            main.saveKits();
        }
    }

    public void delete() {
        if (armorStand == null) return;
        armorStand.remove();
    }

    public void giveToPlayer(Player player) {
        player.getInventory().setContents(contents);
    }

    public String getName() {
        return name;
    }

    public String getNameUncolorized() {
        return StringUtils.removeColor(name);
    }

    public ItemStack getHand() {
        return hand;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ArrayList<ItemStack> getContents() {
        ArrayList<ItemStack> inv = new ArrayList<>(Arrays.asList(contents));

        return inv;
    }
}
