package me.matzhilven.war.commands.subcommands.kits;

import me.matzhilven.war.WARPlugin;
import me.matzhilven.war.commands.SubCommand;
import me.matzhilven.war.utils.StringUtils;
import me.matzhilven.war.war.kit.ArmorstandKit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListKitsSubCommand implements SubCommand {

    private final WARPlugin main;

    public ListKitsSubCommand(WARPlugin main) {
        this.main = main;
    }

    @Override
    public void onCommand(Player sender, Command command, String[] args) {

        List<String> msg = new ArrayList<>();
        List<ArmorstandKit> kits = new ArrayList<>(main.getKitManager().getKits());

        for (String s : main.getMessages().getStringList("kits")) {
            if (s.contains("%kit")) {
                int i = Integer.parseInt(s.split("%kit_")[1].substring(0, 1));
                if (i <= kits.size()) {
                    msg.add(s.replace("%kit_" + i + "%", kits.get((i - 1)).getName()));
                }
            } else {
                msg.add(s);
            }
        }

        StringUtils.sendMessage(sender, msg);
    }

    @Override
    public String getPermission() {
        return null;
    }
}
