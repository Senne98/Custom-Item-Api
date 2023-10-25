package org.super_man2006.custom_item_api.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class CustomBlockBreakEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private boolean dropItems;
    protected NamespacedKey block;
    private boolean cancel;
    private Location location;

    public CustomBlockBreakEvent(BlockBreakEvent e, NamespacedKey block) {
        this.player = e.getPlayer();
        this.dropItems = e.isDropItems();
        this.block = block;
        this.location = e.getBlock().getLocation();
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isDropItems() {
        return dropItems;
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    public NamespacedKey getBlock() {
        return block;
    }

    public void setBlock(NamespacedKey block) {
        this.block = block;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancel = cancelled;
    }
}
