package me.matzhilven.war.inventories;

import me.matzhilven.war.utils.ItemBuilder;
import me.matzhilven.war.war.kit.ArmorstandKit;
import me.matzhilven.war.war.spawns.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class ChooseSpawnMenu extends Menu {

    private final ArmorstandKit kit;

    public ChooseSpawnMenu(Player p, ArmorstandKit kit) {
        super(p);
        this.kit = kit;
    }

    @Override
    public String getMenuName() {
        return main.getConfig().getString("spawn-gui.name");
    }

    @Override
    public int getSlots() {
        return main.getConfig().getInt("spawn-gui.slots");
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        Optional<Spawn> spawn = main.getSpawnManager().bySlot(e.getSlot());
        if (!spawn.isPresent()) return;

        p.closeInventory();
        p.teleport(spawn.get().getLocation());
        p.getInventory().clear();
        kit.giveToPlayer(p);
    }

    @Override
    public void setMenuItems() {
        main.getSpawnManager().getSpawns().forEach(spawn -> {
            inventory.setItem(spawn.getSlot(), new ItemBuilder(spawn.getIcon())
                    .setName(spawn.getName())
                    .setLore(spawn.getLore())
                    .toItemStack()
            );
        });
    }
}
