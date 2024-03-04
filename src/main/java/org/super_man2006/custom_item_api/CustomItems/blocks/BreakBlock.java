package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.utils.dataTypes.UuidDataType;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.events.CustomBlockBreakEvent;
import org.super_man2006.custom_item_api.pdc.PersistentData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation();

        PersistentDataContainer container = PersistentData.getPersistentDataContainer(location);
        if (!(container.has(new NamespacedKey(CustomItemApi.plugin, "namespacedkey"), PersistentDataType.STRING) && CustomBlock.isCustomBlock(NamespacedKey.fromString(container.get(new NamespacedKey(CustomItemApi.plugin, "namespacedkey"), PersistentDataType.STRING))))) return;

        e.setDropItems(false);

        World world = location.getWorld();

        CustomBlock customBlock = CustomBlock.fromNamespacedKey(NamespacedKey.fromString(container.get(new NamespacedKey(CustomItemApi.plugin, "namespacedkey"), PersistentDataType.STRING)));

        CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(e, customBlock.getKey());
        if (!customBlockBreakEvent.callEvent()) {
            e.setCancelled(true);
        }

        if (customBlock.hasCustomItem()) {
            NamespacedKey customItemKey = customBlock.getCustomItem();
            ItemStack dropItem = CustomItem.getItemStack(customItemKey);

            if (e.getPlayer().getGameMode() == GameMode.SURVIVAL && dropItem != null) {
                Item item = (Item) world.spawnEntity(location.add(.5, .5, .5), EntityType.DROPPED_ITEM);
                item.setItemStack(dropItem);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "X"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "X"), new UuidDataType()));
        }
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType()));
        }
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType()));
        }
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType()));
        }
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType()));
        }
        if (container.has(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType())) {
            uuidList.add(container.get(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType()));
        }

        uuidList.forEach(uuid1 -> {
            ItemDisplay display = (ItemDisplay) world.getEntity(uuid1);
            display.remove();
        });

        container.remove(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "customItem"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "x"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "y"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "z"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "minx"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "miny"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "minz"));
        container.remove(new NamespacedKey(CustomItemApi.plugin, "face"));

        PersistentData.setPersistentDataContainer(location, container);
    }
}
