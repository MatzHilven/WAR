package me.matzhilven.war.listeners;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.inventories.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListeners implements Listener {

    private final WARPlugin main;

    public InventoryListeners(WARPlugin main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof Menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
            ((Menu) e.getInventory().getHolder()).handleClick(e);
        }
    }
}
