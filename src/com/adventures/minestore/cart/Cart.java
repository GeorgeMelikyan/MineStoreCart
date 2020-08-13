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
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                            + "= '%s'", p.getDisplayName()));

            Inventory i = Bukkit.createInventory(null, 3 * 9, config.getTitle());

            int position;
            while (resultSet.next()) {
                position = resultSet.getRow();
                createProduct(i, resultSet.getString("product.name"), config.getRank_lore(), position);
            }

            p.openInventory(i);

            statement.close();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    private void createProduct(Inventory i, String item_name, List<String> item_lore, int pos) {
        ItemStack product = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = product.getItemMeta();
        meta.setDisplayName(item_name);
        ArrayList<String> lore = new ArrayList<>(item_lore);
        meta.setLore(lore);
        product.setItemMeta(meta);

        SkullMeta skullMeta = (SkullMeta) product.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", "http" +
                "://textures.minecraft.net/texture/515dcb2da02cf734829e1e273e3025617d8071516f953251b52545da8d3e8db8").getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        product.setItemMeta(skullMeta);

        i.setItem(pos, product);
    }

    public static GameProfile getNonPlayerProfile(String skinURL, boolean randomName) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), randomName ? getRandomString(16) : null);
        newSkinProfile.getProperties().put("textures", new Property("textures",
                Base64Coder.encodeString("{textures" + ":{SKIN:{url:\"" + skinURL + "\"}}}")));
        return newSkinProfile;
    }

    public static String getRandomString(int length) {
        StringBuilder b = new StringBuilder(length);
        for (int j = 0; j < length; j++)
            b.append(chars.charAt(random.nextInt(chars.length())));
        return b.toString();
    }

}
