package me.matzhilven.war.listeners;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.data.temp.TempPlayerData;
import me.matzhilven.war.inventories.ChooseSpawnMenu;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import me.matzhilven.war.war.kit.ArmorstandKit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final WARPlugin main;

    public PlayerListeners(WARPlugin main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        main.getDb().addPlayer(e.getPlayer());

        if (main.getCurrentWar() == null) return;
        Player player = e.getPlayer();
        War war = main.getCurrentWar();
        if (!(war.canJoin(player))) return;
        war.addPlayer(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        main.getDb().savePlayer(e.getPlayer());
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!main.getClanManager().getClan(p).isPresent()) return;

        Clan clan = main.getClanManager().getClan(p).get();

        if (e.getMessage().startsWith("!") || clan.getInChat().contains(p.getUniqueId())) {
            String msg = main.getMessages().getString("chat-prefix")
                    .replace("%rank%", getRank(p, clan))
                    .replace("%message%", e.getMessage())
                    .replace("%player%", e.getPlayer().getName());

            if (e.getMessage().startsWith("!")) {
                System.out.println("t");
                msg = msg.replaceFirst("!", "");
            }

            e.setCancelled(true);
            String finalMsg = msg;
            clan.getOnline().forEach(player -> StringUtils.sendMessage(player, finalMsg));
        }
    }

    @EventHandler
    private void onPlayerDeath(EntityDeathEvent e) {
        if (main.getCurrentWar() == null) return;

        if (!(e.getEntity() instanceof Player)) return;
        if (e.getEntity().getKiller() == null) return;

        Player player = (Player) e.getEntity();
        Player killer = e.getEntity().getKiller();

        War war = main.getCurrentWar();
        if (!(war.isIn(player) && war.isIn(killer))) return;

        war.addDeath(war.getClan(player));
        war.addKill(war.getClan(killer));

        TempPlayerData.get(player.getUniqueId()).addDeath();
        TempPlayerData.get(killer.getUniqueId()).addKill();

        war.updateScoreboard(player);
        war.updateScoreboard(killer);
    }

    @EventHandler
    private void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (main.getCurrentWar() == null) return;

        if (e.getEntity() instanceof Player) {
            if (!(e.getDamager() instanceof Player)) return;
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            War war = main.getCurrentWar();
            if (!(war.isIn(player) && war.isIn(damager))) return;

            if (war.shareClan(player, damager)) {
                e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof ArmorStand) {
            if (!e.getEntity().hasMetadata("kit")) return;
            Player player = (Player) e.getDamager();

            e.setCancelled(true);

            ArmorstandKit kit = main.getKitManager().byName(e.getEntity().getMetadata("kit").get(0).asString()).get();
            kit.giveToPlayer(player);
            Location spawn = main.getSpawnManager().getRandom();
            if (spawn == null) {
                System.out.println(String.format("[%s] No spawns found!", main.getDataFolder().getName()));
                return;
            }

            player.teleport(spawn);
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof ArmorStand)) return;
        if (!e.getRightClicked().hasMetadata("kit")) return;
        e.setCancelled(true);
        new ChooseSpawnMenu(e.getPlayer(),
                main.getKitManager().byName(e.getRightClicked().getMetadata("kit").get(0).asString()).get())
                .open();
    }

    private String getRank(Player p, Clan clan) {
        if (clan.getLeader().toString().equals(p.getUniqueId().toString())) return "L";
        if (clan.getCoLeaders().contains(p.getUniqueId())) return "CO";
        return "M";
    }
}
