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
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation();

        Chunk chunk = location.getChunk();
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        Set<NamespacedKey> keys = container.getKeys();
        List<NamespacedKey> keyList = keys.stream().toList();

        AtomicBoolean contains = new AtomicBoolean();
        contains.set(false);

        keyList.forEach(namespacedKey -> {
            if (namespacedKey.getKey().equals(String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey)) {
                contains.set(true);
            }
        });

        if (!contains.get()) {
            return;
        }

        NamespacedKey locationKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey);
        NamespacedKey uuidKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey);

        e.setDropItems(false);

        World world = location.getWorld();
        UUID uuid = container.get(uuidKey, new UuidDataType());
        ItemDisplay itemDisplay = (ItemDisplay) world.getEntity(uuid);

        PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();

        CustomItem customItem = null;

        if (dataContainer.has(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"))) {
            customItem = new CustomItem(NamespacedKey.fromString(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING)));
        }

        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Item item = (Item) world.spawnEntity(location.add(.5, .5, .5), EntityType.DROPPED_ITEM);
            item.setItemStack(customItem.getItemstack());
        }

        container.remove(locationKey);
        container.remove(uuidKey);

        PersistentDataContainer displayContainer = itemDisplay.getPersistentDataContainer();

        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType()));
        uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType()));
        uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType()));
        uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType()));
        uuidList.add(displayContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType()));

        uuidList.forEach(uuid1 -> {
            ItemDisplay display = (ItemDisplay) world.getEntity(uuid1);
            display.remove();
        });

        itemDisplay.remove();

    }
}
