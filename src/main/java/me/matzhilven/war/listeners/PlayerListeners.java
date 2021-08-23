package me.matzhilven.war.listeners;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.clan.Clan;
import me.matzhilven.war.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListeners implements Listener {

    private final WARPlugin main;

    public PlayerListeners(WARPlugin main) {
        this.main = main;
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
                    ;

            if (e.getMessage().startsWith("!")) {
                System.out.println("t");
                msg = msg.replaceFirst("!", "");
            }

            e.setCancelled(true);
            String finalMsg = msg;
            clan.getOnline().forEach(player -> StringUtils.sendMessage(player, finalMsg));
        }
    }

    private String getRank(Player p, Clan clan) {
        if (clan.getLeader().toString().equals(p.getUniqueId().toString())) return "L";
        if (clan.getCoLeaders().contains(p.getUniqueId())) return "CO";
        return "M";
    }
}
