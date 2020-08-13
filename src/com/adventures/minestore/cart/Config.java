package com.adventures.minestore.cart;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private static ConfigurationSection mysql;
    private static String rank_name, case_name, money_name, title;
    private static List<String> rank_lore, case_lore, money_lore;

    public Config(MineStore plugin) {
        FileConfiguration config = plugin.getConfig();

        mysql = config.getConfigurationSection("settings.mysql");
        title = config.getString("cart.title");
        rank_lore = config.getStringList("cart.rank-lore");
        rank_name = config.getString("cart.rank-name");
        case_lore = config.getStringList("cart.case-lore");
        case_name = config.getString("cart.case-name");
        money_lore = config.getStringList("cart.money-lore");
        money_name = config.getString("cart.money-name");

    }

    public ConfigurationSection getMysql() {
        return mysql;
    }

    public String getRank_name() {
        return rank_name;
    }

    public String getCase_name() {
        return case_name;
    }

    public String getMoney_name() {
        return money_name;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getRank_lore() {
        return rank_lore;
    }

    public List<String> getCase_lore() {
        return case_lore;
    }

    public List<String> getMoney_lore() {
        return money_lore;
    }
}
