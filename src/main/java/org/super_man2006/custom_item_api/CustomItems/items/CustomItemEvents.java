package org.super_man2006.custom_item_api.CustomItems.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.super_man2006.custom_item_api.events.CustomItemInteractEvent;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomItemEvents implements Listener {

    @EventHandler
    public void onVanillaInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        if (!CustomItem.isCustomItem(e.getItem())) {
            return;
        }

        CustomItemInteractEvent customItemInteractEvent = new CustomItemInteractEvent(e, CustomItem.getCodeName(e.getItem()));

        if (!customItemInteractEvent.callEvent()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(CustomItemInteractEvent e) {
        if (!CustomItem.instances.containsKey(e.getItem())) return;

        CustomItem customItem = CustomItem.fromNamespaceKey(e.getItem());
        Method[] methods = CustomItem.instances.get(customItem.getKey()).getActions().getMethods();
        Arrays.stream(methods).forEach(method -> {
            if (method.getName().equals("Click")) {
                try {
                    method.invoke(CustomItem.instances.get(customItem.getKey()).getActions().getConstructor().newInstance(), e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && customItem.getCommands().get("left_click") != null && !customItem.getCommands().get("left_click").equals("")) {
            e.getPlayer().performCommand(customItem.getCommands().get("left_click"));
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && customItem.getCommands().get("right_click") != null && !customItem.getCommands().get("right_click").equals("")) {
            e.getPlayer().performCommand(customItem.getCommands().get("right_click"));
        }
    }
}
