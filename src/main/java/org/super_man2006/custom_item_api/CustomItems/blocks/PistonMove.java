package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PistonMove implements Listener {

    @EventHandler
    public void extend(BlockPistonExtendEvent e) {

        //List<Location> blocks = new ArrayList<>();
        List<Block> blockList = e.getBlocks();

        AtomicBoolean cancel = new AtomicBoolean(false);

        blockList.forEach(block -> {
            if (CustomBlock.isCustomBlock(block.getLocation().toBlockLocation())) {
                cancel.set(true);
            }
        });

        e.setCancelled(cancel.get());
    }

    @EventHandler
    public void retract(BlockPistonRetractEvent e) {

        if (!e.isSticky()) {
            return;
        }

        //List<Location> blocks = new ArrayList<>();
        List<Block> blockList = e.getBlocks();

        AtomicBoolean cancel = new AtomicBoolean(false);

        blockList.forEach(block -> {
            if (CustomBlock.isCustomBlock(block.getLocation().toBlockLocation())) {
                cancel.set(true);
            }
        });

        e.setCancelled(cancel.get());
    }
}
