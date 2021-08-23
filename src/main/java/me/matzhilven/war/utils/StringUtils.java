package me.matzhilven.war.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        return s.stream().map(StringUtils::colorize).collect(Collectors.toList());
    }

    public static String removeColor(String s) {
        return ChatColor.stripColor(colorize(s));
    }

    public static String decolorize(String s) {
        return ChatColor.stripColor(s);
    }

    public static List<String> decolorize(List<String> s) {
        return s.stream().map(ChatColor::stripColor).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, String m) {
        sender.sendMessage(colorize(m));
    }

    public static void sendMessage(CommandSender sender, List<String> m) {
        m.forEach(msg -> sendMessage(sender, msg));
    }

    public static String format(int c) {
        return NumberFormat.getNumberInstance(Locale.US).format(c);
    }

    public static String format(long c) {
        return NumberFormat.getNumberInstance(Locale.US).format(c);
    }

    public static String addCommas(List<String> list) {
        StringBuilder result = new StringBuilder();
        for(String string : list) {
            result.append(string);
            result.append(",");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1): "";
    }

    public static String to1String(List<UUID> uuids) {
        return uuids.stream().map(UUID::toString).collect(Collectors.joining(","));
    }

    public static List<UUID> toUUIDList(String uuids) {
        List<UUID> rUUIDs = new ArrayList<>();
        if (uuids == null || uuids.equals("")) return rUUIDs;

        for (String uuid : uuids.split(",")) {
            rUUIDs.add(UUID.fromString(uuid.trim()));
        }
        return rUUIDs;
    }

}
