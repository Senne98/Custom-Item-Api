package org.super_man2006.custom_item_api.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CustomBlockPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected boolean cancel;
    protected boolean canBuild;
    protected Block placedAgainst;
    protected ItemStack itemInHand;
    protected Player player;
    protected EquipmentSlot hand;
    protected NamespacedKey block;
    private Location location;

    public CustomBlockPlaceEvent(BlockPlaceEvent e, NamespacedKey block) {
        placedAgainst = e.getBlockAgainst();
        this.block = block;
        canBuild = e.canBuild();
        hand = e.getHand();
        itemInHand = e.getItemInHand();
        player = e.getPlayer();
        location = e.getBlock().getLocation();
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public Block getPlacedAgainst() {
        return placedAgainst;
    }

    public void setPlacedAgainst(Block placedAgainst) {
        this.placedAgainst = placedAgainst;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public void setHand(EquipmentSlot hand) {
        this.hand = hand;
    }

    public NamespacedKey getBlock() {
        return block;
    }

    public void setBlock(NamespacedKey block) {
        this.block = block;
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