package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.scheduler.BukkitScheduler;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.events.CustomBlockPlaceEvent;

import java.util.List;

import static org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock.fromLocation;
import static org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock.isCustomBlock;

public class DisplayEntityControl implements Listener {
    @EventHandler
    public void blockForm(BlockFormEvent e) {
        onPlace(e.getBlock().getLocation());
    }

    @EventHandler
    public void blockGrow(BlockGrowEvent e) {
        onPlace(e.getBlock().getLocation());
    }

    @EventHandler
    public void blockMultiPlace(BlockMultiPlaceEvent e) {
        onPlace(e.getBlock().getLocation());
    }

    @EventHandler
    public void blockPistonExtend(BlockPistonExtendEvent e) {
        onPlace(e.getBlock().getLocation());
        e.getBlocks().forEach(block -> onPlace(block.getLocation()));
    }

    @EventHandler
    public void blockPistonRetract(BlockPistonRetractEvent e) {
        onPlace(e.getBlock().getLocation());
        e.getBlocks().forEach(block -> onPlace(block.getLocation()));
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        onPlace(e.getBlockPlaced().getLocation());
    }

    @EventHandler
    public void blockSpread(BlockSpreadEvent e) {
        onPlace(e.getBlock().getLocation());
    }

    @EventHandler
    public void entityBlockForm(EntityBlockFormEvent e) {
        onPlace(e.getBlock().getLocation());
    }

    @EventHandler
    public void customBlockPlace(CustomBlockPlaceEvent e) {
        onPlace(e.getLocation());
    }

    public void onPlace(Location location) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.runTaskLater(CustomItemApi.plugin, () -> {
            for (BlockFace face : List.of(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)) {
                if (!isCustomBlock(location.getBlock().getRelative(face).getLocation())) continue;
                fromLocation(location.getBlock().getRelative(face).getLocation()).removeUnneededDisplays(location.getBlock().getRelative(face).getLocation());
            }
        }, 1L);
    }

}
