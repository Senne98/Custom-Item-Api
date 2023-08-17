package org.super_man2006.custom_item_api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.super_man2006.custom_item_api.CustomItems.blocks.BreakBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.LightUpdate;
import org.super_man2006.custom_item_api.CustomItems.blocks.PlaceBlock;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;

import java.util.HashMap;

public final class CustomItemApi extends JavaPlugin {

    public static HashMap<NamespacedKey, CustomBlock> cBlockList = new HashMap<>();
    public static HashMap<NamespacedKey, CustomItem> cItemList = new HashMap<>();

    public static String locationKey = "locationkey";
    public static String uuidKey = "uuidkey";
    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        /*NamespacedKey blockKey = new NamespacedKey(plugin, "Test");
        CustomBlock customBlock = new CustomBlock(Material.FURNACE, blockKey);
        customBlock.setRotation(CustomBlock.Rotation.AROUND_Y);

        NamespacedKey itemKey = new NamespacedKey(plugin, "Test");
        CustomItem customItem = new CustomItem(Material.FURNACE, itemKey);
        customItem.setCustomBlock(blockKey);
        customItem.setName(Component.text("Beautiful Furnace"));*/

        //Events
        getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new LightUpdate(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
