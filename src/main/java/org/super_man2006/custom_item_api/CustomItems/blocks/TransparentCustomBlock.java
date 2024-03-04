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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.utils.dataTypes.UuidDataType;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.pdc.PersistentData;
import org.super_man2006.custom_item_api.utils.MaterialUtils;
import org.super_man2006.custom_item_api.utils.dataTypes.VectorDataType;
import org.super_man2006.custom_item_api.utils.dataTypes.LocationArrayDataType;
import org.super_man2006.custom_item_api.utils.dataTypes.LocationDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TransparentCustomBlock extends CustomBlock {

    public TransparentCustomBlock(Material material, NamespacedKey key, Class actions, Material placedBlock) {
        super(material, key, actions, placedBlock);

        instances.put(key, this);
    }

    public TransparentCustomBlock(Material material, NamespacedKey key, Class actions) {
        super(material, key, actions);

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

        TransparentCustomBlock customBlock = new TransparentCustomBlock(material, key, actions, placedBlock);
        customBlock.setCmd(cmd);
        customBlock.setCmdBoolean(cmdBoolean);
        customBlock.setCommands(commands);
        customBlock.setCustomItem(drop);
        customBlock.setRotation(rotation);

        instances.put(key, customBlock);
    }

    @Override
    public void place(Location location, BlockFace blockFace, Player player) {

        World world = location.getWorld();
        Block block = world.getBlockAt(location);
        block.setType(getPlacedBlock());
        BlockData blockData = block.getBlockData();
        world.setBlockData(location, blockData);

        Location displayLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

        ItemDisplay itemDisplayX = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
        itemDisplayX.setItemStack(super.getItemstack());
        itemDisplayX.setBillboard(Display.Billboard.FIXED);

        PersistentDataContainer dataContainer = PersistentData.getPersistentDataContainer(location);

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, getKey().toString());
        if (getCustomItem() != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, getCustomItem().toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "x"), new UuidDataType(), itemDisplayX.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");
        itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");

        if (getRotation() == Rotation.ALL_BLOCKFACE) {
            AxisAngle4f leftRotation = leftRotationCalculation(blockFace);
            Transformation transformation = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformation);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            //dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());

        } else if (getRotation() == Rotation.AROUND_Y){
            BlockFace face = player.getFacing().getOppositeFace();
            AxisAngle4f leftRotation = leftRotationCalculation(face);

            Transformation transformationX = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformationX);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ()));
            //dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), face.getDirection());
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

    @Override
    public void place(Location location, BlockFace blockFace) {

        World world = location.getWorld();
        Block block = world.getBlockAt(location);
        block.setType(getPlacedBlock());
        BlockData blockData = block.getBlockData();
        world.setBlockData(location, blockData);

        Location displayLocation = new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);

        ItemDisplay itemDisplayX = (ItemDisplay) world.spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplayX.setItemStack(getItemstack());
        itemDisplayX.setBillboard(Display.Billboard.FIXED);

        PersistentDataContainer dataContainer = PersistentData.getPersistentDataContainer(location);

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, getKey().toString());
        if (getCustomItem() != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, getCustomItem().toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "x"), new UuidDataType(), itemDisplayX.getUniqueId());
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");
        itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");


        if (getRotation() == Rotation.ALL_BLOCKFACE) {
            AxisAngle4f leftRotation = leftRotationCalculation(blockFace);

            Transformation transformationX = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformationX);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new LocationDataType(), new Location(displayLocation.getWorld(), displayLocation.getX(), displayLocation.getY(), displayLocation.getZ()));
            //itemDisplayX.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());
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

    @Override
    public void removeUnneededDisplays(Location location) {
        if (!isCustomBlock(location)) return;

        boolean canRemove = true;

        List<BlockFace> faces = List.of(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

        for (BlockFace blockFace : faces) {
            if (MaterialUtils.isTransparent(location.getBlock().getRelative(blockFace).getType())) {
                canRemove = false;
                break;
            }
        }

        if (canRemove) {
            UUID display = PersistentData.getPersistentDataContainer(location).get(new NamespacedKey(CustomItemApi.plugin, "x"), new UuidDataType());
            Bukkit.getEntity(display).remove();

            PersistentDataContainer persistentData = PersistentData.getPersistentDataContainer(location);
            persistentData.remove(new NamespacedKey(CustomItemApi.plugin, "x"));
            PersistentData.setPersistentDataContainer(location, persistentData);
        }
    }
}
