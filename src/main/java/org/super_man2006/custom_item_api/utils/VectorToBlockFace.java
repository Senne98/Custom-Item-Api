package org.super_man2006.custom_item_api.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class VectorToBlockFace {

    public static BlockFace get(Vector vector) {
        if (vector == new Vector(0, 1, 0)) {
            return BlockFace.UP;
        } else if (vector == new Vector(0, -1, 0)) {
            return BlockFace.DOWN;
        } else if (vector == new Vector(0, 0, -1)) {
            return BlockFace.NORTH;
        } else if (vector == new Vector(0, 0, 1)) {
            return BlockFace.SOUTH;
        } else if (vector == new Vector(1, 0, 0)) {
            return BlockFace.EAST;
        } else if (vector == new Vector(-1, 0, 0)) {
            return BlockFace.WEST;
        } else {
            return null;
        }
    }
}
