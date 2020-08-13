package com.adventures.minestore.cart;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MineStore extends JavaPlugin {
    public static Logger logger = Bukkit.getLogger();
    public static MineStore instance;

    public static MineStore getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        try {
            getCommand("cart").setExecutor(new Cart(this));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Cart loaded.");
    }
}
