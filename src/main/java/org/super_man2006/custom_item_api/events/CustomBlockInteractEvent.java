package org.super_man2006.custom_item_api.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * CustomBlockInteractEvent
 * Can sometimes be called twice!!!
 */
public class CustomBlockInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private boolean hasBlock;
    protected ItemStack item;
    protected Action action;
    protected BlockFace blockFace;
    private Result useClickedBlock;
    private Result useItemInHand;
    private EquipmentSlot hand;
    private Location interactionPoint;
    protected NamespacedKey block;
    private boolean cancel;
    private Location location;

    public CustomBlockInteractEvent(PlayerInteractEvent e, NamespacedKey block) {
        this.player = e.getPlayer();
        this.hasBlock = e.hasBlock();
        this.block = block;
        this.location = e.getClickedBlock().getLocation();
        this.item = e.getItem();
        this.action = e.getAction();
        this.blockFace = e.getBlockFace();
        this.useClickedBlock = e.useInteractedBlock();
        this.useItemInHand = e.useItemInHand();
        this.hand = e.getHand();
        this.interactionPoint = e.getInteractionPoint();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isHasBlock() {
        return hasBlock;
    }

    public void setHasBlock(boolean hasBlock) {
        this.hasBlock = hasBlock;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public Result getUseClickedBlock() {
        return useClickedBlock;
    }

    public void setUseClickedBlock(Result useClickedBlock) {
        this.useClickedBlock = useClickedBlock;
    }

    public Result getUseItemInHand() {
        return useItemInHand;
    }

    public void setUseItemInHand(Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public void setHand(EquipmentSlot hand) {
        this.hand = hand;
    }

    public Location getInteractionPoint() {
        return interactionPoint;
    }

    public void setInteractionPoint(Location interactionPoint) {
        this.interactionPoint = interactionPoint;
    }

    public NamespacedKey getBlock() {
        return block;
    }

    public void setBlock(NamespacedKey block) {
        this.block = block;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
