package org.super_man2006.custom_item_api.CustomItems.blocks;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;
import org.super_man2006.custom_item_api.utils.VectorDataType;
import org.super_man2006.custom_item_api.utils.VectorToBlockFace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PistonMove implements Listener {

    @EventHandler
    public void extend(BlockPistonExtendEvent e) {

        //e.setCancelled(true);

        List<Location> blocks = new ArrayList<>();
        List<Block> blockList = e.getBlocks();

        AtomicBoolean cancel = new AtomicBoolean(false);

        blockList.forEach(block -> {
            if (CustomBlock.isCustomBlock(block.getLocation().toBlockLocation())) {
                cancel.set(true);
            }
        });

        e.setCancelled(cancel.get());

        if (true) {
            return;
        }

        blockList.forEach(block -> {
            blocks.add(block.getLocation().add(e.getDirection().getDirection().multiply(-1)));
            Bukkit.getServer().sendMessage(Component.text(block.getLocation().toString()));
        });

        Bukkit.getServer().sendMessage(Component.text(e.getDirection().getDirection().toString()));

        pistonMove(blocks, e.getDirection());

    }

    @EventHandler
    public void retract(BlockPistonRetractEvent e) {

        if (!e.isSticky()) {
            return;
        }

        List<Location> blocks = new ArrayList<>();
        List<Block> blockList = e.getBlocks();

        AtomicBoolean cancel = new AtomicBoolean(false);

        blockList.forEach(block -> {
            if (CustomBlock.isCustomBlock(block.getLocation().toBlockLocation())) {
                cancel.set(true);
            }
        });

        e.setCancelled(cancel.get());

        if (true) {
            return;
        }

        e.getBlocks().forEach(block -> blocks.add(block.getLocation()));

        pistonMove(blocks, e.getDirection());

    }

    private void pistonMove(List<Location> blocks, BlockFace direction) {

        Vector directionVec = direction.getDirection();

        blocks.forEach( location -> {

            if (CustomBlock.isCustomBlock(location)) {

                Chunk chunk = location.getChunk();
                PersistentDataContainer container = chunk.getPersistentDataContainer();

                if (CustomBlock.isCustomBlock(location)) {

                    NamespacedKey locationKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey);
                    NamespacedKey uuidKey = new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.getBlockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey);

                    World world = location.getWorld();
                    UUID uuid = container.get(uuidKey, new UuidDataType());
                    ItemDisplay itemDisplay = (ItemDisplay) world.getEntity(uuid);

                    PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();

                    CustomBlock block = new CustomBlock(NamespacedKey.fromString(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING)));;

                    container.remove(locationKey);
                    container.remove(uuidKey);

                    List<UUID> uuidList = new ArrayList<>();
                    uuidList.add(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType()));
                    uuidList.add(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType()));
                    uuidList.add(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType()));
                    uuidList.add(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType()));
                    uuidList.add(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType()));

                    Vector blockFace = dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType());

                    uuidList.forEach(uuid1 -> {
                        ItemDisplay display = (ItemDisplay) world.getEntity(uuid1);
                        display.remove();
                    });

                    itemDisplay.remove();

                    Bukkit.getScheduler().runTaskLater(CustomItemApi.plugin, new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.place(location.add(directionVec), VectorToBlockFace.get(blockFace));
                        }
                    }, 1);
                }

            }
        });

    }
}
