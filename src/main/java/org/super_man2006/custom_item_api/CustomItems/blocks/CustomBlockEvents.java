package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.super_man2006.custom_item_api.events.CustomBlockBreakEvent;
import org.super_man2006.custom_item_api.events.CustomBlockInteractEvent;
import org.super_man2006.custom_item_api.events.CustomBlockPlaceEvent;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomBlockEvents implements Listener {

    @EventHandler
    public void onInteractVanilla(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;

        if (!CustomBlock.isCustomBlock(e.getClickedBlock().getLocation())) {
            return;
        }

        CustomBlock customBlock = CustomBlock.fromLocation(e.getClickedBlock().getLocation());

        CustomBlockInteractEvent customBlockInteractEvent = new CustomBlockInteractEvent(e, customBlock.getKey());
        if (!customBlockInteractEvent.callEvent()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(CustomBlockInteractEvent e) {
        if (!CustomBlock.instances.containsKey(e.getBlock())) return;

        CustomBlock customBlock = CustomBlock.fromNamespacedKey(e.getBlock());
        Method[] methods = CustomBlock.instances.get(e.getBlock()).getActions().getMethods();
        Arrays.stream(methods).forEach(method -> {
            if (method.getName().equals("Click")) {
                try {
                    method.invoke(CustomBlock.instances.get(customBlock.getKey()).getActions().getConstructor().newInstance(), e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        if (customBlock.getCommands().get("click") != null && !customBlock.getCommands().get("click").equals("")) {
            e.getPlayer().performCommand(customBlock.getCommands().get("click"));
        }
    }

    @EventHandler
    public void onBreak(CustomBlockBreakEvent e) {
        if (!CustomBlock.instances.containsKey(e.getBlock())) return;

        CustomBlock customBlock = CustomBlock.fromNamespacedKey(e.getBlock());
        Method[] methods = CustomBlock.instances.get(customBlock.getKey()).getActions().getMethods();
        Arrays.stream(methods).forEach(method -> {
            if (method.getName().equals("Break")) {
                try {
                    method.invoke(CustomBlock.instances.get(customBlock.getKey()).getActions().getConstructor().newInstance(), e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        if (customBlock.getCommands().get("break") != null && !customBlock.getCommands().get("break").equals("")) {
            e.getPlayer().performCommand(customBlock.getCommands().get("break"));
        }
    }

    @EventHandler
    public void onPlace(CustomBlockPlaceEvent e) {
        if (!CustomBlock.instances.containsKey(e.getBlock())) return;

        CustomBlock customBlock = CustomBlock.fromNamespacedKey(e.getBlock());
        Method[] methods = CustomBlock.instances.get(customBlock.getKey()).getActions().getMethods();
        Arrays.stream(methods).forEach(method -> {
            if (method.getName().equals("Place")) {
                try {
                    method.invoke(CustomBlock.instances.get(customBlock.getKey()).getActions().getConstructor().newInstance(), e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        if (customBlock.getCommands().get("place") != null && !customBlock.getCommands().get("place").equals("")) {
            e.getPlayer().performCommand(customBlock.getCommands().get("place"));
        }
    }
}
