package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.events.CustomBlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation();

        Chunk chunk = location.getChunk();
        PersistentDataContainer container = chunk.getPersistentDataContainer();

        AtomicBoolean contains = new AtomicBoolean();
        contains.set(false);

        if (!container.has(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey))) {
            return;
        }

        NamespacedKey locationKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey);
        NamespacedKey uuidKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey);

        e.setDropItems(false);

        World world = location.getWorld();
        UUID uuid = container.get(uuidKey, new UuidDataType());
        ItemDisplay itemDisplay = (ItemDisplay) world.getEntity(uuid);

        PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();

        CustomBlock customBlock;
        CustomItem customItem;

        if (dataContainer.has(new NamespacedKey(CustomItemApi.plugin, "namespacedKey") )) {
            customBlock = CustomBlock.fromNamespacedKey(NamespacedKey.fromString(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING)));
        } else {
            return;
        }


        CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(e, customBlock.getKey());
        if (!customBlockBreakEvent.callEvent()) {
            e.setCancelled(true);
        }


        if (customBlock.hasCustomItem()) {
            customItem = CustomItem.fromNamespaceKey(customBlock.getCustomItem());

            if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                Item item = (Item) world.spawnEntity(location.add(.5, .5, .5), EntityType.DROPPED_ITEM);
                item.setItemStack(customItem.getItemstack());
            }
        }

        container.remove(locationKey);
        container.remove(uuidKey);

        PersistentDataContainer displayContainer = itemDisplay.getPersistentDataContainer();

        List<UUID> uuidList = new ArrayList<>();
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "X"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "X"), new UuidDataType()));
        }
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType()));
        }
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType()));
        }
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType()));
        }
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType()));
        }
        if (displayContainer.has(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType())) {
            uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType()));
        }

        uuidList.forEach(uuid1 -> {
            ItemDisplay display = (ItemDisplay) world.getEntity(uuid1);
            display.remove();
        });

        itemDisplay.remove();
    }
}
