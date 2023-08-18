package org.super_man2006.custom_item_api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.super_man2006.custom_item_api.CustomItems.blocks.BreakBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.LightUpdate;
import org.super_man2006.custom_item_api.CustomItems.blocks.PlaceBlock;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.files.Read;
import org.super_man2006.custom_item_api.files.Save;

import java.io.File;
import java.util.HashMap;

public final class CustomItemApi extends JavaPlugin {

    public static HashMap<NamespacedKey, CustomBlock> cBlockList = new HashMap<>();
    public static HashMap<NamespacedKey, CustomItem> cItemList = new HashMap<>();

    public static String locationKey = "locationkey";
    public static String uuidKey = "uuidkey";
    public static Plugin plugin;
    public static File blocksFile;
    public static File itemsFile;
    public static File settingsFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        saveResource("blocks.json", false);
        saveResource("items.json", false);
        saveResource("Settings.json", false);
        blocksFile = new File(getDataFolder(),"blocks.json");
        itemsFile = new File(getDataFolder(),"items.json");
        settingsFile = new File(getDataFolder(),"Settings.json");

        ConfigurationSerialization.registerClass(CustomItem.class);
        ConfigurationSerialization.registerClass(CustomBlock.class);
        Read.read();

        //bStats
        int pluginId = 19559; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

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
        Save.save();
    }
}
