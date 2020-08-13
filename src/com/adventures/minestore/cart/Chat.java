package com.adventures.minestore.cart;

import org.bukkit.ChatColor;

public class Chat {
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String[] color(String[] msg) {
        for (int i = 0; i < msg.length; i++) {
            msg[i] = ChatColor.translateAlternateColorCodes('&', msg[i]);
        }
        return msg;
    }
}
