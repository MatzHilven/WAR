package me.matzhilven.war.war.kit;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.utils.ConfigUtils;
import me.matzhilven.war.utils.Logger;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class KitManager {

    private final WARPlugin main;
    private final FileConfiguration kitsF;

    private final Set<ArmorstandKit> kits;

    public KitManager(WARPlugin main) {
        this.main = main;
        this.kitsF = main.getKits();
        this.kits = new HashSet<>();

        Logger.log("Loading kits...");
        loadKits();
    }

    private void loadKits() {
        if (kitsF.get("kits") == null) return;

        kitsF.getConfigurationSection("kits").getKeys(false).forEach(kit -> {

            ItemStack[] array = ConfigUtils.toArray((ArrayList<ItemStack>) kitsF.get("kits." + kit + ".inventory"));

            ArmorstandKit armorstandKit = new ArmorstandKit(
                    kitsF.getString("kits." + kit + ".name"),
                    kitsF.getItemStack("kits." + kit + ".hand"),
                    kitsF.getItemStack("kits." + kit + ".helmet"),
                    kitsF.getItemStack("kits." + kit + ".chestplate"),
                    kitsF.getItemStack("kits." + kit + ".leggings"),
                    kitsF.getItemStack("kits." + kit + ".boots"),
                    array
            );


            if (kitsF.getString("kits." + kit + ".location") != null) {
                armorstandKit.setSpawnLocation(ConfigUtils.toLocation(kitsF.getString("kits." + kit + ".location")), false);
            }

            Logger.log(" Loaded kit " + armorstandKit.getNameUncolorized());

            kits.add(armorstandKit);
        });
    }

    public Optional<ArmorstandKit> byName(String name) {
        return kits.stream().filter(kit -> StringUtils.removeColor(kit.getName()).equalsIgnoreCase(name)).findFirst();
    }

    public void addKit(ArmorstandKit kit) {
        kits.add(kit);

        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".name", kit.getName());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".hand", kit.getHand());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".helmet", kit.getHelmet());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".chestplate", kit.getChestplate());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".leggings", kit.getLeggings());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".boots", kit.getBoots());
        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase() + ".inventory", kit.getContents());
        main.saveKits();
    }

    public void removeKit(ArmorstandKit kit) {
        kits.remove(kit);

        main.getKits().set("kits." + kit.getNameUncolorized().toLowerCase(), null);
        main.saveKits();
    }

    public Set<ArmorstandKit> getKits() {
        return kits;
    }

    public void spawnKits() {
        kits.forEach(ArmorstandKit::spawn);
    }

    public void removeKits() {
        kits.forEach(ArmorstandKit::delete);
    }
}
