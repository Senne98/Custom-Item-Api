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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.utils.dataTypes.LocationDataType;

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

            int chunckLocX = x%16;
            int chunckLocZ = z%16;

            Arrays.stream((new Location(world, x, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                if (entity instanceof ItemDisplay) {
                    entityList.add((ItemDisplay) entity);
                }
            });

            if (chunckLocX > 0) {
                Arrays.stream((new Location(world, x + 16, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if (chunckLocX < 14) {
                Arrays.stream((new Location(world, x - 16, 1, z)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if (chunckLocZ > 0) {
                Arrays.stream((new Location(world, x, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if (chunckLocZ < 14) {
                Arrays.stream((new Location(world, x, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if ((15 - chunckLocX) + (15 - chunckLocZ) > 14) {
                Arrays.stream((new Location(world, x + 16, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if ((15 - chunckLocX) + chunckLocZ > 14) {
                Arrays.stream((new Location(world, x + 16, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if (chunckLocZ + (15 - chunckLocZ) > 14) {
                Arrays.stream((new Location(world, x - 16, 1, z + 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            if (chunckLocX + chunckLocZ > 14) {
                Arrays.stream((new Location(world, x - 16, 1, z - 16)).getChunk().getEntities()).toList().forEach(entity -> {
                    if (entity instanceof ItemDisplay) {
                        entityList.add((ItemDisplay) entity);
                    }
                });
            }

            entityList.forEach(display -> {
                if (display.getPersistentDataContainer().has(new NamespacedKey(CustomItemApi.plugin, "lightlocation"))) {
                    if (display.getPersistentDataContainer().has(new NamespacedKey(CustomItemApi.plugin, "blocktype")) && display.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING).equals("transparent")) {

                        Location lightLoc = display.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType());

                        int highestSkyLightLevel = 0;
                        int highestBlockLightLevel = 0;

                        Block lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX() + 1, lightLoc.getY(), lightLoc.getZ()).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX() - 1, lightLoc.getY(), lightLoc.getZ()).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX(), lightLoc.getY() + 1, lightLoc.getZ()).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX(), lightLoc.getY() - 1, lightLoc.getZ()).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX(), lightLoc.getY(), lightLoc.getZ() + 1).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX(), lightLoc.getY(), lightLoc.getZ() - 1).getBlock();
                        if (lightBlock.getLightFromBlocks() > highestBlockLightLevel) {
                            highestBlockLightLevel = lightBlock.getLightFromBlocks();
                        }
                        if (lightBlock.getLightFromSky() > highestSkyLightLevel) {
                            highestSkyLightLevel = lightBlock.getLightFromSky();
                        }

                        if (highestBlockLightLevel < 1) {
                            highestBlockLightLevel = 1;
                        }
                        if (highestSkyLightLevel < 1) {
                            highestSkyLightLevel = 1;
                        }

                        display.setBrightness(new Display.Brightness(highestBlockLightLevel - 1, highestSkyLightLevel - 1));

                    } else {

                        Location lightLoc = display.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType());
                        Block lightBlock = new Location(lightLoc.getWorld(), lightLoc.getX(), lightLoc.getY(), lightLoc.getZ()).getBlock();

                        display.setBrightness(new Display.Brightness(lightBlock.getLightFromBlocks(), lightBlock.getLightFromSky()));
                    }
                }
            });
        }, 1);
    }
}
