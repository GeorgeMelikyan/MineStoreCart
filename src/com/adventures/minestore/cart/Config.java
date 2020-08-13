package com.adventures.minestore.cart;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private static ConfigurationSection mysql;
    private static String rank_name, package_name, token_name, title;
    private static List<String> rank_lore, package_lore, token_lore;

    public Config(MineStore plugin) {
        FileConfiguration config = plugin.getConfig();

        mysql = config.getConfigurationSection("settings.mysql");
        title = config.getString("cart.title");
        rank_lore = config.getStringList("cart.rank-lore");
        rank_name = config.getString("cart.rank-name");
        package_lore = config.getStringList("cart.package-lore");
        package_name = config.getString("cart.package-name");
        token_lore = config.getStringList("cart.token-lore");
        token_name = config.getString("cart.token-name");

    }

    public ConfigurationSection getMysql() {
        return mysql;
    }

    public String getRank_name() {
        return rank_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public String getToken_name() {
        return token_name;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getRank_lore() {
        return rank_lore;
    }

    public List<String> getPackage_lore() {
        return package_lore;
    }

    public List<String> getToken_lore() {
        return token_lore;
    }
}
