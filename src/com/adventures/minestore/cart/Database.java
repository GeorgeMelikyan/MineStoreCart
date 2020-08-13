package com.adventures.minestore.cart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database {
    private final MineStore plugin;
    private final String url;
    private final Config config;

    public Database(MineStore plugin) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.plugin = plugin;
        config = new Config(plugin);
        url = "jdbc:mysql://" + config.getMysql().get("host") + ":" + config.getMysql().get("port") + "/" + config.getMysql().get("database");
        plugin.getLogger().log(Level.INFO, url);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }

    public Connection getConnection() throws SQLException {
        plugin.getLogger().log(Level.INFO, url);
        return DriverManager.getConnection(this.url, config.getMysql().get("username").toString(),
                config.getMysql().get("password").toString());
    }
}
