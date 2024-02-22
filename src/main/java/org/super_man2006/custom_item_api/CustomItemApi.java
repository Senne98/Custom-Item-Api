package org.super_man2006.custom_item_api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.super_man2006.custom_item_api.CustomItems.blocks.*;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItemEvents;
import org.super_man2006.custom_item_api.CustomItems.load.LoadBlocks;
import org.super_man2006.custom_item_api.CustomItems.load.LoadItems;
import org.super_man2006.custom_item_api.commands.customApi.CustomApiCommands;
import org.super_man2006.custom_item_api.commands.customApi.CustomApiTabComplete;
import org.super_man2006.custom_item_api.permissions.Permissions;

import java.io.File;
import java.io.IOException;

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

        //data
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        //packs
        File packFolder = new File(getDataFolder(), "packs");
        if (!packFolder.exists()) {
            packFolder.mkdirs();
        }

        try {
            LoadBlocks.load();
            LoadItems.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //VersionCheck
        new VersionCheck();


        //Events
        getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new BreakBlock(), this);
        getServer().getPluginManager().registerEvents(new LightUpdate(), this);
        getServer().getPluginManager().registerEvents(new PistonMove(), this);
        getServer().getPluginManager().registerEvents(new CustomBlockEvents(), this);
        getServer().getPluginManager().registerEvents(new CustomItemEvents(), this);
        getServer().getPluginManager().registerEvents(new Permissions(), this);

        //Permissions
        getServer().getPluginManager().addPermission(Permissions.getCustomApiOp());
        getServer().getPluginManager().addPermission(Permissions.getCustomApiGiveCmd());
        File permissionsFile = new File(getDataFolder(), "data/permissions.json");
        if (!permissionsFile.exists()) {
            saveResource("data/permissions.json", false);
        }
        Permissions.load();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, () -> Permissions.save(), 20L * 20L, 20L * 60L * 5L);

        //Commands
        getCommand("CustomApi").setExecutor(new CustomApiCommands());

        //TabComplete
        getCommand("CustomApi").setTabCompleter(new CustomApiTabComplete());

        /*CustomBlock customBlock = new TransparentCustomBlock(Material.FURNACE, new NamespacedKey(plugin, "Test"), new actionTest().getClass(), Material.GLASS)
                .setRotation(CustomBlock.Rotation.AROUND_Y).setDropItem(new NamespacedKey(plugin, "Test"));

        CustomItem customItem = new CustomItem(Material.FURNACE, new NamespacedKey(plugin, "Test"), new CustomItemActions() {
            @Override
            public void Click(CustomItemInteractEvent e) {
                e.getPlayer().sendMessage(Component.text("Placed"));
            }
        })
                .setCustomBlock(new NamespacedKey(plugin, "Test")).setName(Component.text("Beautiful Furnace"));*/
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Permissions.save();
    }
}
