package org.super_man2006.custom_item_api;

import org.bukkit.block.data.type.Bell;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.UUID;

public class Permissions implements Listener {

    public static HashMap<UUID, PermissionAttachment> attachments = new HashMap<>();

    private static Permission CustomApiOp = new Permission("customitemapi.op", PermissionDefault.FALSE);
    private static Permission CustomApiGiveCmd = new Permission("customitemapi.givecmd", PermissionDefault.FALSE);

    public static Permission getCustomApiOp() {
        return CustomApiOp;
    }

    public static Permission getCustomApiGiveCmd() {
        return CustomApiGiveCmd;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PermissionAttachment attachment = e.getPlayer().addAttachment(CustomItemApi.plugin);
        attachments.put(e.getPlayer().getUniqueId(), attachment);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        attachments.remove(e.getPlayer().getUniqueId());
    }
}
