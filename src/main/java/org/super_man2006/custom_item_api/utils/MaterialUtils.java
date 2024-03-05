package org.super_man2006.custom_item_api.utils;

import org.bukkit.Material;

public class MaterialUtils {

    public static boolean isTransparent(Material material) {
        if (!material.isSolid()) return true;
        if (material.isAir()) return true;
        if (material.getKey().toString().toLowerCase().contains("glass")) return true;
        if (material.getKey().toString().toLowerCase().contains("cauldron")) return true;
        return false;
    }
}
