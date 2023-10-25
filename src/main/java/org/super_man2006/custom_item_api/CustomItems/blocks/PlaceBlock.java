package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;

import java.util.Collection;

public class PlaceBlock implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null) {
            return;
        }
        if (!e.getAction().isRightClick()) {
            return;
        }
        if (e.getItem() == null || !CustomItem.isCustomItem(e.getItem())) {
            return;
        }

        Location location = e.getClickedBlock().getLocation();
        if (location.getBlock().getType().isInteractable() && !e.getPlayer().isSneaking()) {
            return;
        }

        LightUpdate.onLightUpdate(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().toBlockLocation()));
        e.setCancelled(true);

        CustomItem customItem = CustomItem.fromNamespaceKey(CustomItem.getCodeName(e.getItem()));
        if (!customItem.hasCustomBlock()) {
            return;
        }

        Location entityTest = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        entityTest.add(.5, .5, .5);
        Collection<LivingEntity> entities = entityTest.getNearbyLivingEntities(5);
        BoundingBox blockBox = location.getBlock().getBoundingBox();

        BlockFace face = e.getBlockFace();
        if (!Tag.REPLACEABLE.isTagged(location.getBlock().getType())) {
            location.add(face.getDirection());
            blockBox.expandDirectional(face.getDirection());
        }
        if (!Tag.REPLACEABLE.isTagged(location.getBlock().getType())) {
            return;
        }

        boolean overlap;
        overlap = entities.stream().anyMatch(livingEntity -> {

            if (blockBox.overlaps(livingEntity.getBoundingBox())) {
                return true;
            }

            return false;
        });

        if (overlap) {
            return;
        }

        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            ItemStack usedItem = e.getPlayer().getInventory().getItem(e.getHand());
            usedItem.setAmount(e.getPlayer().getInventory().getItem(e.getHand()).getAmount() - 1);
            e.getPlayer().getInventory().setItem(e.getHand(), usedItem);
        }

        NamespacedKey cBlock = customItem.getCBlock();
        CustomBlock customBlock = CustomBlock.fromNamespacedKey(cBlock);

        customBlock.place(location, e.getBlockFace(), e.getPlayer());

    }

    /*@EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CustomItem customItem = new CustomItem(new NamespacedKey(CustomItemApi.plugin, "Test"));
        ItemStack itemStack = customItem.getItemstack();
        itemStack.setAmount(64);
        e.getPlayer().getInventory().addItem(itemStack);
    }*/
}
