package org.super_man2006.custom_item_api.permissions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.super_man2006.custom_item_api.CustomItemApi;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Permissions implements Listener {

    public static HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();

    private static Permission CustomApiOp = new Permission("customitemapi.op", PermissionDefault.FALSE);
    private static Permission CustomApiGiveCmd = new Permission("customitemapi.givecmd", PermissionDefault.FALSE);
    private static List<UUID> opPlayers = new ArrayList<>();
    private static List<UUID> giveCmdPlayers = new ArrayList<>();
    private static boolean permChanged;

    public static Permission getCustomApiOp() {
        return CustomApiOp;
    }

    public static void giveCustomApiOp(Player player) {
        permChanged = true;

        Permissions.attachments.get(player.getUniqueId()).setPermission(CustomApiOp, true);
        opPlayers.add(player.getUniqueId());
    }

    public static void removeCustomApiOp(Player player) {
        permChanged = true;

        Permissions.attachments.get(player.getUniqueId()).setPermission(CustomApiOp, true);
        if (opPlayers.contains(player.getUniqueId())) {
            opPlayers.remove(player.getUniqueId());
        }
    }

    public static Permission getCustomApiGiveCmd() {
        return CustomApiGiveCmd;
    }

    public static void giveCustomApiGiveCmd(Player player) {
        permChanged = true;

        Permissions.attachments.get(player.getUniqueId()).setPermission(CustomApiGiveCmd, true);
        giveCmdPlayers.add(player.getUniqueId());
    }

    public static void removeCustomApiGiveCmd(Player player) {
        permChanged = true;

        Permissions.attachments.get(player.getUniqueId()).setPermission(CustomApiGiveCmd, true);
        if (giveCmdPlayers.contains(player.getUniqueId())) {
            giveCmdPlayers.remove(player.getUniqueId());
        }
    }

    public static void load() {
        permChanged = false;

        File permissionsFile = new File(CustomItemApi.plugin.getDataFolder(), "data/permissions.json");
        try {
            JsonReader reader = new JsonReader(new FileReader(permissionsFile));
            Gson gson = new Gson();

            JsonObject permissions = gson.fromJson(reader, JsonObject.class);
            if (permissions == null) return;

            if (permissions.get("op") != null) {
                JsonArray op = gson.fromJson(permissions.get("op"), JsonArray.class);
                op.forEach(jsonElement -> opPlayers.add(UUID.fromString(jsonElement.getAsString())));
            }
            if (permissions.get("giveCmd") != null) {
                JsonArray giveCmd = gson.fromJson(permissions.get("giveCmd"), JsonArray.class);
                giveCmd.forEach(jsonElement -> giveCmdPlayers.add(UUID.fromString(jsonElement.getAsString())));
            }

            CustomItemApi.plugin.getLogger().info("Loaded permissions");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save() {
        if (!permChanged) return;
        permChanged = false;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(new File(CustomItemApi.plugin.getDataFolder(), "data/permissions.json")));

            JsonObject permissions = new JsonObject();

            JsonArray op = new JsonArray();
            opPlayers.forEach(uuid -> op.add(uuid.toString()));
            permissions.add("op", op);

            JsonArray giveCmd = new JsonArray();
            giveCmdPlayers.forEach(uuid -> op.add(uuid.toString()));
            permissions.add("giveCmd", giveCmd);

            gson.toJson(permissions, writer);
            writer.flush();

            CustomItemApi.plugin.getLogger().info("Saved permissions");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PermissionAttachment attachment = e.getPlayer().addAttachment(CustomItemApi.plugin);
        attachments.put(e.getPlayer().getUniqueId(), attachment);

        if (opPlayers.contains(e.getPlayer().getUniqueId())) {
            giveCustomApiOp(e.getPlayer());
        }
        if (giveCmdPlayers.contains(e.getPlayer().getUniqueId())) {
            giveCustomApiGiveCmd(e.getPlayer());
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        attachments.remove(e.getPlayer().getUniqueId());
    }
}
