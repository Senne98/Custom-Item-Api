package org.super_man2006.custom_item_api.CustomItems.blocks;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.super_man2006.custom_item_api.Coordinates.Coordinates;
import org.super_man2006.custom_item_api.Coordinates.CoordinatesDataType;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomBlock {
    private Material material;
    private int cmd;
    private boolean cmdBoolean;
    private NamespacedKey customItem;
    private boolean dropVanillaBoolean;
    private Material dropVanilla;
    private NamespacedKey key;
    private Rotation rotation;
    private Material placedBlock;

    public CustomBlock(Material material, NamespacedKey key, int cmd) {
        if (CustomItemApi.cBlockList.containsKey(key)) {
            return;
        }

        this.material = material;
        this.key = key;
        this.cmd = cmd;
        cmdBoolean = true;
        customItem = null;
        dropVanillaBoolean = false;
        rotation = Rotation.ALL_BLOCKFACE;

        CustomItemApi.cBlockList.put(key, this);
    }

    public CustomBlock(Material material, NamespacedKey key) {
        if (CustomItemApi.cBlockList.containsKey(key)) {
            return;
        }

        this.material = material;
        this.key = key;
        cmdBoolean = false;
        customItem = null;
        dropVanillaBoolean = false;
        rotation = Rotation.ALL_BLOCKFACE;

        CustomItemApi.cBlockList.put(key, this);
    }

    CustomBlock(NamespacedKey key) {
        if (!CustomItemApi.cBlockList.containsKey(key)) {
            return;
        }

        this.material = CustomItemApi.cBlockList.get(key).getMaterial();
        this.cmd = CustomItemApi.cBlockList.get(key).getCmd();
        this.cmdBoolean = CustomItemApi.cBlockList.get(key).getCmdBoolean();
        this.customItem = CustomItemApi.cBlockList.get(key).getCustomItem();
        this.dropVanillaBoolean = CustomItemApi.cBlockList.get(key).getDropVanillaBoolean();
        this.dropVanilla = CustomItemApi.cBlockList.get(key).dropVanilla;
        this.key = key;
        this.rotation = CustomItemApi.cBlockList.get(key).getRotation();
        this.placedBlock = CustomItemApi.cBlockList.get(key).getPlacedBlock();

    }

    public Material getPlacedBlock() {
        return placedBlock;
    }

    public void setPlacedBlock(Material placedBlock) {
        this.placedBlock = placedBlock;
    }

    /*public void setDropItem(Material material) {
        dropVanillaBoolean = true;
        customItem = null;
        dropVanilla = material;
    }*/

    public void setDropItem(NamespacedKey customItem) {
        dropVanillaBoolean = false;
        this.customItem = customItem;
    }

    public void removeDropItem() {
        dropVanillaBoolean = false;
        customItem = null;
    }

    /*public Material getDropVanilla() {
        if (dropVanillaBoolean) {
            return dropVanilla;
        }
        return null;
    }*/

    public NamespacedKey getDropCustom() {
        if (dropVanillaBoolean) {
            return null;
        }
        if (customItem == null) {
            return null;
        }
        return customItem;
    }

    public void place(Location location, BlockFace blockFace, Player player) {

        World world = location.getWorld();
        Block block = world.getBlockAt(location);
        block.setType(placedBlock);
        BlockData blockData = block.getBlockData();
        world.setBlockData(location, blockData);

        Location displayLocation = new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);

        ItemDisplay itemDisplayX = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayX.setItemStack(getItemstack());
        itemDisplayX.setBillboard(Display.Billboard.FIXED);
        ItemDisplay itemDisplayMinX = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayMinX.setItemStack(getItemstack());
        itemDisplayMinX.setBillboard(Display.Billboard.FIXED);
        ItemDisplay itemDisplayY = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayY.setItemStack(getItemstack());
        itemDisplayY.setBillboard(Display.Billboard.FIXED);
        ItemDisplay itemDisplayMinY = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayMinY.setItemStack(getItemstack());
        itemDisplayMinY.setBillboard(Display.Billboard.FIXED);
        ItemDisplay itemDisplayZ = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayZ.setItemStack(getItemstack());
        itemDisplayZ.setBillboard(Display.Billboard.FIXED);
        ItemDisplay itemDisplayMinZ = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayMinZ.setItemStack(getItemstack());
        itemDisplayMinZ.setBillboard(Display.Billboard.FIXED);

        PersistentDataContainer dataContainer = itemDisplayX.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, key.toString());
        if (customItem != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, customItem.toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "MinX"), new UuidDataType(), itemDisplayMinX.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "Y"), new UuidDataType(), itemDisplayY.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "MinY"), new UuidDataType(), itemDisplayMinY.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "Z"), new UuidDataType(), itemDisplayZ.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "MinZ"), new UuidDataType(), itemDisplayMinZ.getUniqueId());

        if (rotation == Rotation.ALL_BLOCKFACE) {

            AxisAngle4f leftRotation = leftRotationCalculation(blockFace);

            Transformation transformationX = new Transformation(new Vector3f().add(.5f, 0, 0), leftRotation, scaleCalculation("X", blockFace), new AxisAngle4f());
            Transformation transformationMinX = new Transformation(new Vector3f().add(-.5f, 0, 0), leftRotation, scaleCalculation("X", blockFace), new AxisAngle4f());
            Transformation transformationY = new Transformation(new Vector3f().add(0, .5f, 0), leftRotation, scaleCalculation("Y", blockFace), new AxisAngle4f());
            Transformation transformationMinY = new Transformation(new Vector3f().add(0, -.5f, 0), leftRotation, scaleCalculation("Y", blockFace), new AxisAngle4f());
            Transformation transformationZ = new Transformation(new Vector3f().add(0, 0, .5f), leftRotation, scaleCalculation("Z", blockFace), new AxisAngle4f());
            Transformation transformationMinZ = new Transformation(new Vector3f().add(0, 0, -.5f), leftRotation, scaleCalculation("Z", blockFace), new AxisAngle4f());

            itemDisplayX.setTransformation(transformationX);
            itemDisplayMinX.setTransformation(transformationMinX);
            itemDisplayY.setTransformation(transformationY);
            itemDisplayMinY.setTransformation(transformationMinY);
            itemDisplayZ.setTransformation(transformationZ);
            itemDisplayMinZ.setTransformation(transformationMinZ);

            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayMinX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayY.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayMinY.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayZ.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1).getBlock().getLightFromSky()));
            itemDisplayMinZ.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1).getBlock().getLightFromSky()));

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayMinX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()));
            itemDisplayMinY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()));
            itemDisplayZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1));
            itemDisplayMinZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1));

            List<Object> returnVal = new ArrayList<>();
            returnVal.add(location);
            returnVal.add(itemDisplayX.getUniqueId());

        } else if (rotation == Rotation.AROUND_Y){
            BlockFace face = player.getFacing().getOppositeFace();

            AxisAngle4f leftRotation = leftRotationCalculation(face);

            Transformation transformationX = new Transformation(new Vector3f().add(.5f, 0, 0), leftRotation, scaleCalculation("X", face), new AxisAngle4f());
            Transformation transformationMinX = new Transformation(new Vector3f().add(-.5f, 0, 0), leftRotation, scaleCalculation("X", face), new AxisAngle4f());
            Transformation transformationY = new Transformation(new Vector3f().add(0, .5f, 0), leftRotation, scaleCalculation("Y", face), new AxisAngle4f());
            Transformation transformationMinY = new Transformation(new Vector3f().add(0, -.5f, 0), leftRotation, scaleCalculation("Y", face), new AxisAngle4f());
            Transformation transformationZ = new Transformation(new Vector3f().add(0, 0, .5f), leftRotation, scaleCalculation("Z", face), new AxisAngle4f());
            Transformation transformationMinZ = new Transformation(new Vector3f().add(0, 0, -.5f), leftRotation, scaleCalculation("Z", face), new AxisAngle4f());

            itemDisplayX.setTransformation(transformationX);
            itemDisplayMinX.setTransformation(transformationMinX);
            itemDisplayY.setTransformation(transformationY);
            itemDisplayMinY.setTransformation(transformationMinY);
            itemDisplayZ.setTransformation(transformationZ);
            itemDisplayMinZ.setTransformation(transformationMinZ);

            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayMinX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayY.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayMinY.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()).getBlock().getLightFromSky()));
            itemDisplayZ.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1).getBlock().getLightFromSky()));
            itemDisplayMinZ.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1).getBlock().getLightFromSky()));

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayMinX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()));
            itemDisplayMinY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()));
            itemDisplayZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1));
            itemDisplayMinZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1));

            List<Object> returnVal = new ArrayList<>();
            returnVal.add(location);
            returnVal.add(itemDisplayX.getUniqueId());

        }

        Chunk chunk = location.getChunk();

        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey), new CoordinatesDataType(), new Coordinates(location.blockX(), location.blockY(), location.blockZ()));
        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey), new UuidDataType(), itemDisplayX.getUniqueId());
    }

    private static AxisAngle4f leftRotationCalculation(BlockFace face) {
        float angle;
        AxisAngle4f leftRotation;
        float y = 0;
        float x = 0;
        if (face == BlockFace.NORTH) {
            angle = (float) Math.PI;
            y = 1;
        } else if (face == BlockFace.EAST) {
            angle = (float) ((Math.PI) / 2);
            y = 1;
        } else if (face == BlockFace.SOUTH) {
            angle = (float) 0;
            y = 1;
        } else if (face == BlockFace.WEST) {
            angle = (float) ((3 * Math.PI) / 2);
            y = 1;
        } else if (face == BlockFace.UP) {
            angle = (float) ((3 * Math.PI) / 2);
            x = 1;
        } else {
            angle = (float) ((Math.PI) / 2);
            x = 1;
        }
        leftRotation = new AxisAngle4f(angle, x , y, 0);
        return leftRotation;
    }

    private static Vector3f scaleCalculation(String direction, BlockFace face) {
        if (direction.equals("X")) {
            if (face == BlockFace.SOUTH || face == BlockFace.NORTH) {
                return new Vector3f().add(0.001f, 1.001f, 1.001f);
            }
            if (face == BlockFace.EAST || face == BlockFace.WEST) {
                return new Vector3f().add(1.001f, 1.001f, 0.001f);
            }
            return new Vector3f().add(0.001f, 1.001f, 1.001f);
        }

        if (direction.equals("Y")) {
            if (face == BlockFace.SOUTH || face == BlockFace.NORTH) {
                return new Vector3f().add(1.001f, 0.001f, 1.001f);
            }
            if (face == BlockFace.EAST || face == BlockFace.WEST) {
                return new Vector3f().add(1.001f, 0.001f, 1.001f);
            }
            return new Vector3f().add(1.001f, 1.001f, 0.001f);
        }

        if (direction.equals("Z")) {
            if (face == BlockFace.SOUTH || face == BlockFace.NORTH) {
                return new Vector3f().add(1.001f, 1.001f, 0.001f);
            }
            if (face == BlockFace.EAST || face == BlockFace.WEST) {
                return new Vector3f().add(0.001f, 1.001f, 1.001f);
            }
            return new Vector3f().add(1.001f, 0.001f, 1.001f);
        }

        return null;
    }

    public ItemStack getItemstack() {
        ItemStack itemStack = new ItemStack(material);
        if (cmdBoolean) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setCustomModelData(cmd);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCmd() {
        return cmd;
    }

    public boolean getCmdBoolean() {
        return cmdBoolean;
    }

    public NamespacedKey getCustomItem() {
        return customItem;
    }

    public boolean getDropVanillaBoolean() {
        return dropVanillaBoolean;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public static boolean isCustomBlock(Location location) {
        Chunk chunk = location.getChunk();
        PersistentDataContainer dataContainer = chunk.getPersistentDataContainer();
        Set<NamespacedKey> keys = dataContainer.getKeys();
        boolean result;
        result = false;

        if (keys.contains(String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.blockZ()) + CustomItemApi.locationKey)) {
            result = true;
        }

        return result;
    }

    public enum Rotation {
        ALL_BLOCKFACE,
        AROUND_Y,
        //ALL_LOOKING
    }

}
