package org.super_man2006.custom_item_api;

import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlockActions;
import org.super_man2006.custom_item_api.events.CustomBlockBreakEvent;
import org.super_man2006.custom_item_api.events.CustomBlockInteractEvent;
import org.super_man2006.custom_item_api.events.CustomBlockPlaceEvent;

public class actionTest implements CustomBlockActions {

    @Override
    public void Click(CustomBlockInteractEvent e) {
        e.getPlayer().sendMessage("Click");
    }

    @Override
    public void Place(CustomBlockPlaceEvent e) {
        e.getPlayer().sendMessage("Place");
    }

    @Override
    public void Break(CustomBlockBreakEvent e) {
        e.getPlayer().sendMessage("Break");
    }
}
