package com.adventures.minestore.cart;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> color(List<String> msg) {
        List<String> new_msg = new ArrayList<>();
        for (String line : msg) {
            new_msg.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return new_msg;
    }
}
