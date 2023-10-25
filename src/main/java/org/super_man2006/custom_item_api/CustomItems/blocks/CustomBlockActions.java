package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.super_man2006.custom_item_api.events.CustomBlockBreakEvent;
import org.super_man2006.custom_item_api.events.CustomBlockInteractEvent;
import org.super_man2006.custom_item_api.events.CustomBlockPlaceEvent;

public interface CustomBlockActions {

    public abstract void Click(CustomBlockInteractEvent e);

    public abstract void Place(CustomBlockPlaceEvent e);

    public abstract void Break(CustomBlockBreakEvent e);
}
