package org.super_man2006.custom_item_api.CustomItems.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.super_man2006.custom_item_api.events.CustomItemInteractEvent;

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
        CustomItem.instances.get(e.getItem()).getActions().Click(e);
    }
}
