package org.super_man2006.custom_item_api.CustomItems.blocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.pdc.PersistentData;
import org.super_man2006.custom_item_api.utils.MaterialUtils;
import org.super_man2006.custom_item_api.utils.VectorDataType;
import org.super_man2006.custom_item_api.utils.dataTypes.LocationArrayDataType;
import org.super_man2006.custom_item_api.utils.dataTypes.LocationDataType;

import java.util.*;

public class CustomBlock {

    public static HashMap<NamespacedKey, CustomBlock> instances = new HashMap<>();

    private Material material; //
    private int cmd; //
    private boolean cmdBoolean; //
    private NamespacedKey customItem; //
    private NamespacedKey key; //
    private Rotation rotation; //
    private Material placedBlock; //
    private Class actions;

    private HashMap<String, String> commands = new HashMap<>();

    public CustomBlock(Material material, NamespacedKey key, Class actions, Material placedBlock) {
        if (instances.containsKey(key)) {
            throw new IllegalArgumentException("CustomBlock with key " + key.toString() + " already exists!");
        }
        if (!CustomBlockActions.class.isAssignableFrom(actions)) {
            throw new IllegalArgumentException("CustomBlockActions must be implemented!");
        }

        this.actions = actions;
        this.placedBlock = placedBlock;
        this.material = material;
        this.key = key;
        cmdBoolean = false;
        customItem = null;
        rotation = Rotation.ALL_BLOCKFACE;
        commands.put("break", "");
        commands.put("place", "");
        commands.put("click", "");

        instances.put(key, this);
    }

    public CustomBlock(Material material, NamespacedKey key, Class actions) {
        if (instances.containsKey(key)) {
            throw new IllegalArgumentException("CustomBlock with key " + key.toString() + " already exists!");
        }
        if (!CustomBlockActions.class.isAssignableFrom(actions)) {
            throw new IllegalArgumentException("CustomBlockActions must be implemented!");
        }

        this.actions = actions;
        this.placedBlock = Material.STONE;
        this.material = material;
        this.key = key;
        cmdBoolean = false;
        customItem = null;
        rotation = Rotation.ALL_BLOCKFACE;
        commands.put("break", "");
        commands.put("place", "");
        commands.put("click", "");

        instances.put(key, this);
    }

    public static void fromJson(String json, NamespacedKey key) throws ClassNotFoundException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        boolean cmdBoolean;
        int cmd;
        if (jsonObject.get("texture").getAsJsonObject().has("cmd")) {
            cmdBoolean = true;
            cmd = jsonObject.get("texture").getAsJsonObject().get("cmd").getAsInt();
        } else {
            cmdBoolean = false;
            cmd = 0;
        }

        Material material = CustomItem.getItemStack(NamespacedKey.fromString(jsonObject.get("texture").getAsJsonObject().get("material").getAsString())).getType();
        Material placedBlock = CustomItem.getItemStack(NamespacedKey.fromString(jsonObject.get("placement").getAsJsonObject().get("placed_block").getAsString())).getType();
        Rotation rotation = Rotation.valueOf(jsonObject.get("placement").getAsJsonObject().get("rotation").getAsString());
        HashMap<String, String> commands = gson.fromJson(jsonObject.get("commands").getAsJsonObject(), HashMap.class);
        Class actions = Class.forName(jsonObject.get("actions_class").getAsString());
        NamespacedKey drop = NamespacedKey.fromString(jsonObject.get("drop").getAsString().toLowerCase());

        CustomBlock customBlock = new CustomBlock(material, key, actions, placedBlock);
        customBlock.setCmd(cmd);
        customBlock.setCmdBoolean(cmdBoolean);
        customBlock.setCommands(commands);
        customBlock.setCustomItem(drop);
        customBlock.setRotation(rotation);

        instances.put(key, customBlock);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public void setCmdBoolean(boolean cmdBoolean) {
        this.cmdBoolean = cmdBoolean;
    }

    public void setCustomItem(NamespacedKey customItem) {
        this.customItem = customItem;
    }

    public void setKey(NamespacedKey key) {
        this.key = key;
    }

    public HashMap<String, String> getCommands() {
        return commands;
    }

    public void setCommands(HashMap<String, String> commands) {
        this.commands = commands;
    }

    public static CustomBlock fromNamespacedKey(NamespacedKey key) {
        if (!instances.containsKey(key)) {
            return null;
        }

        return instances.get(key);

    }

    public static CustomBlock fromLocation(Location location) {
        PersistentDataContainer container = PersistentData.getPersistentDataContainer(location);

        if (!(container.has(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING) && isCustomBlock(NamespacedKey.fromString(container.get(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING))))) {
            return null;
        }

        return fromNamespacedKey(NamespacedKey.fromString(container.get(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING)));
    }

    public Class getActions() {
        return actions;
    }

    public CustomBlock setActions(Class actions) {
        this.actions = actions;
        return this;
    }

    public boolean hasCustomItem() {
        if (customItem == null) {
            return false;
        }
        return true;
    }

    public Material getPlacedBlock() {
        return placedBlock;
    }

    public CustomBlock setPlacedBlock(Material placedBlock) {
        this.placedBlock = placedBlock;
        return this;
    }

    public CustomBlock setDropItem(NamespacedKey item) {
        this.customItem = item;
        return this;
    }

    public void removeDropItem() {
        customItem = null;
    }

    public NamespacedKey getDropCustom() {
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

        PersistentDataContainer dataContainer = PersistentData.getPersistentDataContainer(location);

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, key.toString());
        if (customItem != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, customItem.toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "x"), new UuidDataType(), itemDisplayX.getUniqueId());
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

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayMinX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()));
            itemDisplayMinY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()));
            itemDisplayZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1));
            itemDisplayMinZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());

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

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayMinX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()));
            itemDisplayMinY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()));
            itemDisplayZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1));
            itemDisplayMinZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), face.getDirection());
        }

        Chunk chunk = location.getChunk();
        Location[] locationsOld = chunk.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "customblocklocations"), new LocationArrayDataType());

        Location[] locationsNew;
        if (locationsOld == null) {
            locationsNew = new Location[1];
            locationsNew[0] = location;
        } else {
            locationsNew = Arrays.copyOf(locationsOld, locationsOld.length + 1);
            locationsNew[locationsOld.length] = location;
        }
        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "customblocklocations"), new LocationArrayDataType(), locationsNew);

        PersistentData.setPersistentDataContainer(location, dataContainer);

        removeUnneededDisplays(location);
    }

    public void place(Location location, BlockFace blockFace) {

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

        PersistentDataContainer dataContainer = PersistentData.getPersistentDataContainer(location);

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, key.toString());
        if (customItem != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, customItem.toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "X"), new UuidDataType(), itemDisplayX.getUniqueId());
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

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayMinX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() - 1, displayLocation.getY(), displayLocation.getZ()));
            itemDisplayY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() + 1, displayLocation.getZ()));
            itemDisplayMinY.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY() - 1, displayLocation.getZ()));
            itemDisplayZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() + 1));
            itemDisplayMinZ.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ() - 1));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());
        }

        Chunk chunk = location.getChunk();
        Location[] locationsOld = chunk.getPersistentDataContainer().get(new NamespacedKey(CustomItemApi.plugin, "customblocklocations"), new LocationArrayDataType());

        Location[] locationsNew;
        if (locationsOld == null) {
            locationsNew = new Location[1];
            locationsNew[0] = location;
        } else {
            locationsNew = Arrays.copyOf(locationsOld, locationsOld.length + 1);
            locationsNew[locationsOld.length] = location;
        }
        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "customblocklocations"), new LocationArrayDataType(), locationsNew);

        PersistentData.setPersistentDataContainer(location, dataContainer);

        removeUnneededDisplays(location);
    }

    public void removeUnneededDisplays(Location location) {
        if (!isCustomBlock(location)) return;

        PersistentDataContainer persistentData = PersistentData.getPersistentDataContainer(location);

        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "x"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.EAST).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "x"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "x"));
            }
        }
        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "y"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.UP).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "y"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "y"));
            }
        }
        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "z"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.SOUTH).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "z"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "z"));
            }
        }
        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "minx"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.WEST).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "minx"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "minx"));
            }
        }
        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "miny"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.DOWN).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "miny"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "miny"));
            }
        }
        if (persistentData.has(new NamespacedKey(CustomItemApi.plugin, "minz"))) {
            if (!MaterialUtils.isTransparent(location.getBlock().getRelative(BlockFace.NORTH).getType())) {
                UUID display = persistentData.get(new NamespacedKey(CustomItemApi.plugin, "minz"), new UuidDataType());
                Bukkit.getEntity(display).remove();
                persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "minz"));
            }
        }

        PersistentData.setPersistentDataContainer(location, persistentData);
    }

    static AxisAngle4f leftRotationCalculation(BlockFace face) {
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

    static Vector3f scaleCalculation(String direction, BlockFace face) {
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

    public CustomBlock setRotation(Rotation rotation) {
        this.rotation = rotation;
        return this;
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

    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Checks if the {@link Location} has a custom block.
     * @param location
     * @return true if location has a custom block.
     */
    public static boolean isCustomBlock(Location location) {
        if (PersistentData.getPersistentDataContainer(location).has(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING) && isCustomBlock(NamespacedKey.fromString(PersistentData.getPersistentDataContainer(location).get(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING)))) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the {@link NamespacedKey} is associated with a customBlock
     * @param key
     * @return true if there is a custom block with the provided key.
     */
    public static boolean isCustomBlock(NamespacedKey key) {
        if (instances.containsKey(key)) {
            return true;
        }
        return false;
    }

    public enum Rotation {
        ALL_BLOCKFACE,
        AROUND_Y;
        //ALL_LOOKING
    }
}
