package org.super_man2006.custom_item_api.CustomItems.blocks;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.block.DragonEggFormEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.super_man2006.custom_item_api.Coordinates.Coordinates;
import org.super_man2006.custom_item_api.Coordinates.CoordinatesDataType;
import org.super_man2006.custom_item_api.CustomItemApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LightUpdate implements Listener {

    @EventHandler
    public void blockBreakBlock(BlockBreakBlockEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void dragonEggForm(DragonEggFormEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockBurn(BlockBurnEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockCanBuildHere(BlockCanBuildEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockFade(BlockFadeEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockForm(BlockFormEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockGrow(BlockGrowEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockIgnite(BlockIgniteEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockMultiPlace(BlockMultiPlaceEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockPistonExtend(BlockPistonExtendEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockPistonRetract(BlockPistonRetractEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        onLightUpdate(e.getBlockPlaced());
    }

    @EventHandler
    public void blockRedstone(BlockRedstoneEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockSpread(BlockSpreadEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void campfireStart(CampfireStartEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void cauldronLevelChange(CauldronLevelChangeEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void entityBlockForm(EntityBlockFormEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void fluidLevelChange(FluidLevelChangeEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void inventoryBlockStart(InventoryBlockStartEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void tntPrime(TNTPrimeEvent e) {
        onLightUpdate(e.getBlock());
    }

    @EventHandler
    public void blockDestroy(BlockDestroyEvent e) {
        onLightUpdate(e.getBlock());
    }

    public static void onLightUpdate(Block block) {

        BukkitScheduler scheduler = CustomItemApi.plugin.getServer().getScheduler();

        scheduler.runTaskLater(CustomItemApi.plugin, () -> {
            List<ItemDisplay> entityList = new ArrayList<>();

            int x = block.getX();
            int z = block.getZ();
            World world = block.getWorld();

            Arrays.stream((new Location(world, x, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x + 16, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x - 16, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x + 16, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x + 16, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x - 16, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            Arrays.stream((new Location(world, x - 16, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            entityList.forEach(display -> {
                if (display.getPersistentDataContainer().has(new NamespacedKey(CustomItemApi.plugin, "lightlocation"))) {
                    Coordinates coordinates = display.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType());
                    Block lightBlock = new Location(world, coordinates.getX(), coordinates.getY(), coordinates.getZ()).getBlock();

                    display.setBrightness(new Display.Brightness(lightBlock.getLightFromBlocks(), lightBlock.getLightFromSky()));
                }
            });
        }, 1);
    }
}
