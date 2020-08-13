package com.adventures.minestore.cart;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class Cart implements CommandExecutor {
    private final Config config;
    private final Database database;
    private static final Random random = new Random();
    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Cart(MineStore plugin) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        config = new Config(plugin);
        this.database = new Database(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("cart")) return false;

        Player p = (Player) sender;

        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(String.format("SELECT  product.name, product_types.name, " +
                            "product_types.command, purchase.purchase_time, purchase.gived, purchase.amount FROM " +
                            "`purchase` " + "INNER JOIN product ON purchase.product_id = product.id INNER JOIN " +
                            "product_types ON product" + ".product_types_id = product_types.id WHERE purchase.player "
                            + "= '%s' ORDER BY purchase.purchase_time", p.getDisplayName()));

            Inventory inventory = Bukkit.createInventory(null, 3 * 9, Chat.color(config.getTitle()));

            while (resultSet.next()) {
                int position = resultSet.getRow() - 1;
                String product_type = resultSet.getString("product_types.name");
                String texture = null;
                String name = resultSet.getString("product.name");
                Timestamp date = resultSet.getTimestamp("purchase.purchase_time");
                List<String> lore = new ArrayList<>();

                switch (product_type) {
                    case "rank":
                        texture = "http://textures.minecraft" +
                                ".net/texture/734fb3203233efbae82628bd4fca7348cd071e5b7b52407f1d1d2794e31799ff";
                        lore = replaceLorePlaceholder(config.getRank_lore(), date, "1520");
                        break;
                    case "case":
                        texture = "http://textures.minecraft" +
                                ".net/texture/b2c5f7ac706b2e8a878ebf972b07f3d36449ab70b09acd973eeabb0d5fc4a6b4";
                        lore = replaceLorePlaceholder(config.getCase_lore(), date, "1520");
                        break;
                    case "money":
                        texture = "http://textures.minecraft" +
                                ".net/texture/c8ea7933581ee9fb400f39044d3015ca0d43bb6e72fc9267c7fd1361f68ff12b";
                        lore = replaceLorePlaceholder(config.getMoney_lore(), date, "1520");
                        break;
                }
                createProduct(inventory, name, lore, texture, position);
            }

            p.openInventory(inventory);

            statement.close();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    private List<String> replaceLorePlaceholder(List<String> lore, Timestamp date, String price) {
        List<String> result = new ArrayList<>();

        for (String line : lore) {
            String new_line = line.replaceAll("<date>", date.toString());
            new_line = new_line.replaceAll("<price>", price);
            result.add(new_line);
        }

        return result;
    }

    private void createProduct(Inventory i, String item_name, List<String> item_lore, String texture, int pos) {
        ItemStack product = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        //Create lore & name
        ItemMeta meta = product.getItemMeta();
        meta.setDisplayName(Chat.color(item_name));
        ArrayList<String> lore = new ArrayList<>(Chat.color(item_lore));
        meta.setLore(lore);
        product.setItemMeta(meta);

        //Create skull texture
        SkullMeta skullMeta = (SkullMeta) product.getItemMeta();
        setSkullDesign(skullMeta, texture);
        product.setItemMeta(skullMeta);

        i.setItem(pos, product);
    }

    private void setSkullDesign(SkullMeta skullMeta, String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData =
                Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
