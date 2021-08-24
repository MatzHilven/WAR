package me.matzhilven.war.listeners;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.data.temp.TempPlayerData;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.War;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final WARPlugin main;

    public PlayerListeners(WARPlugin main) {
        this.main = main;
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
        war.addKill(war.getClan(player));

        TempPlayerData.get(player.getUniqueId()).addDeath();
        TempPlayerData.get(killer.getUniqueId()).addKill();

        war.updateScoreboard(player);
        war.updateScoreboard(killer);

    }

    private String getRank(Player p, Clan clan) {
        if (clan.getLeader().toString().equals(p.getUniqueId().toString())) return "L";
        if (clan.getCoLeaders().contains(p.getUniqueId())) return "CO";
        return "M";
    }
}
