package org.super_man2006.custom_item_api.pdc;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.custom_item_api.CustomItemApi;

public class PersistentData {

    private static NamespacedKey getKey(Location location) {
        return new NamespacedKey(CustomItemApi.plugin, "pdc-" + location.getBlockX() + location.getBlockY() + location.getBlockZ() + location.getWorld().getName());
    }

    public static PersistentDataContainer getPersistentDataContainer(Location location) {
        PersistentDataContainer chunkData = location.getChunk().getPersistentDataContainer();

        if (!chunkData.has(getKey(location), PersistentDataType.TAG_CONTAINER)) {
            chunkData.set(getKey(location), PersistentDataType.TAG_CONTAINER, new CraftPersistentDataContainer(new CraftPersistentDataTypeRegistry()).getAdapterContext().newPersistentDataContainer());
        }

        return chunkData.get(getKey(location), PersistentDataType.TAG_CONTAINER);
    }

    public static void setPersistentDataContainer(Location location, PersistentDataContainer data) {
        PersistentDataContainer chunkData = location.getChunk().getPersistentDataContainer();
        chunkData.set(getKey(location), PersistentDataType.TAG_CONTAINER, data);
    }
}
