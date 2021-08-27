package me.matzhilven.war.utils;

import me.matzhilven.war.WARPlugin;

public class Logger {

    public static void log(String s) {
        System.out.println(String.format("[%s] " + s, WARPlugin.getPlugin(WARPlugin.class).getDescription().getName()));
    }
}
