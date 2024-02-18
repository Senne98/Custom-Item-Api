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
import org.super_man2006.custom_item_api.Coordinates.Coordinates;
import org.super_man2006.custom_item_api.Coordinates.CoordinatesDataType;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.UuidDataType;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.utils.VectorDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        PersistentDataContainer dataContainer = itemDisplayX.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, getKey().toString());

        if (getCustomItem() != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, getCustomItem().toString());
        }

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");

        if (getRotation() == Rotation.ALL_BLOCKFACE) {
            AxisAngle4f leftRotation = leftRotationCalculation(blockFace);
            Transformation transformation = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformation);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()));
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());

        } else if (getRotation() == Rotation.AROUND_Y){
            BlockFace face = player.getFacing().getOppositeFace();
            AxisAngle4f leftRotation = leftRotationCalculation(face);

            Transformation transformationX = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformationX);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ()));
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), face.getDirection());
        }

        Chunk chunk = location.getChunk();

        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey), new CoordinatesDataType(), new Coordinates(location.blockX(), location.blockY(), location.blockZ()));
        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey), new UuidDataType(), itemDisplayX.getUniqueId());
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

        PersistentDataContainer dataContainer = itemDisplayX.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "namespacedKey"), PersistentDataType.STRING, getKey().toString());
        if (getCustomItem() != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "customItem"), PersistentDataType.STRING, getCustomItem().toString());
        }
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "blocktype"), PersistentDataType.STRING, "transparent");


        if (getRotation() == Rotation.ALL_BLOCKFACE) {
            AxisAngle4f leftRotation = leftRotationCalculation(blockFace);

            Transformation transformationX = new Transformation(new Vector3f().set(0.5f, 0.5f, 0.5f), leftRotation, new Vector3f().set(1.0001f, 1.0001f, 1.0001f), new AxisAngle4f());
            itemDisplayX.setTransformation(transformationX);
            itemDisplayX.setBrightness(new Display.Brightness(new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromBlocks(), new Location(displayLocation.getWorld(), displayLocation.getX() + 1, displayLocation.getY(), displayLocation.getZ()).getBlock().getLightFromSky()));

            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "lightlocation"), new CoordinatesDataType(), new Coordinates(displayLocation.getX(), displayLocation.getY(), displayLocation.getZ()));
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "face"), new VectorDataType(), blockFace.getDirection());

            List<Object> returnVal = new ArrayList<>();
            returnVal.add(location);
            returnVal.add(itemDisplayX.getUniqueId());
        }

        Chunk chunk = location.getChunk();

        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.locationKey), new CoordinatesDataType(), new Coordinates(location.blockX(), location.blockY(), location.blockZ()));
        chunk.getPersistentDataContainer().set(new NamespacedKey(CustomItemApi.plugin, String.valueOf(location.getBlockX()) + String.valueOf(location.blockY()) + String.valueOf(location.getBlockZ()) + CustomItemApi.uuidKey), new UuidDataType(), itemDisplayX.getUniqueId());
    }
}
