package org.super_man2006.custom_item_api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.super_man2006.custom_item_api.CustomItems.blocks.*;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItemActions;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItemEvents;
import org.super_man2006.custom_item_api.commands.Give;
import org.super_man2006.custom_item_api.commands.customApi.CustomApiCommands;
import org.super_man2006.custom_item_api.commands.customApi.CustomApiTabComplete;
import org.super_man2006.custom_item_api.events.CustomItemInteractEvent;

import java.io.File;

public final class CustomItemApi extends JavaPlugin {
    public static String locationKey = "locationkey";
    public static String uuidKey = "uuidkey";
    public static Plugin plugin;
    public static File settingsFile;


    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        saveResource("Settings.json", false);
        settingsFile = new File(getDataFolder(),"Settings.json");

        //bStats
        int pluginId = 19559; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        //Events
        getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new LightUpdate(), this);
        getServer().getPluginManager().registerEvents(new Give(), this);
        getServer().getPluginManager().registerEvents(new PistonMove(), this);
        getServer().getPluginManager().registerEvents(new CustomBlockEvents(), this);
        getServer().getPluginManager().registerEvents(new CustomItemEvents(), this);
        getServer().getPluginManager().registerEvents(new Permissions(), this);

        //Permissions
        getServer().getPluginManager().addPermission(Permissions.getCustomApiOp());
        getServer().getPluginManager().addPermission(Permissions.getCustomApiGiveCmd());

        //Commands
        getCommand("CustomApi").setExecutor(new CustomApiCommands());

        //TabComplete
        getCommand("CustomApi").setTabCompleter(new CustomApiTabComplete());

        CustomBlock customBlock = new TransparentCustomBlock(Material.FURNACE, new NamespacedKey(plugin, "Test"), new actionTest().getClass(), Material.GLASS)
                .setRotation(CustomBlock.Rotation.AROUND_Y).setDropItem(new NamespacedKey(plugin, "Test"));

        CustomItem customItem = new CustomItem(Material.FURNACE, new NamespacedKey(plugin, "Test"), new CustomItemActions() {
            @Override
            public void Click(CustomItemInteractEvent e) {
                e.getPlayer().sendMessage(Component.text("Placed"));
            }
        })
                .setCustomBlock(new NamespacedKey(plugin, "Test")).setName(Component.text("Beautiful Furnace"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
