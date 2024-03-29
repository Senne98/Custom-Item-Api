package org.super_man2006.custom_item_api.CustomItems.items;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.super_man2006.custom_item_api.CustomItemApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CustomItem {

    public static HashMap<NamespacedKey, CustomItem> instances = new HashMap<>();

    boolean nameBoolean;
    boolean cmdBoolean;
    boolean loreBoolean;
    Material material;
    HashMap<String, Integer> intTags = new HashMap<>();
    HashMap<String, Boolean> booleanTags = new HashMap<>();
    HashMap<String, Byte> byteTags = new HashMap<>();
    HashMap<String, byte[]> byteArrayTags = new HashMap<>();
    HashMap<String, Double> doubleTags = new HashMap<>();
    HashMap<String, Float> floatTags = new HashMap<>();
    HashMap<String, int[]> intArrayTags = new HashMap<>();
    HashMap<String, Long> longTags = new HashMap<>();
    HashMap<String, long[]> longArrayTags = new HashMap<>();
    HashMap<String, Short> shortTags = new HashMap<>();
    HashMap<String, String> stringTags = new HashMap<>();
    List<String> tagKeys = new ArrayList<>();
    int cmd;
    Component name;
    int version;
    NamespacedKey key;
    List<Component> lore;
    NamespacedKey customBlock;
    Class actions;
    HashMap<String, String> commands = new HashMap<>();

    public CustomItem(Material material, NamespacedKey key, Class actions) {
        this.actions = actions;
        this.material = material;
        nameBoolean = false;
        cmdBoolean = false;
        version = 0;
        this.key = key;

        instances.put(key, this);
    }

    public HashMap<String, String> getCommands() {
        return commands;
    }

    public void setCommands(HashMap<String, String> commands) {
        this.commands = commands;
    }

    public static void fromJson(String json, NamespacedKey key) throws ClassNotFoundException {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        HashMap<String, Integer> intTags;
        HashMap<String, Boolean> booleanTags;
        HashMap<String, Byte> byteTags;
        HashMap<String, byte[]> byteArrayTags;
        HashMap<String, Double> doubleTags;
        HashMap<String, Float> floatTags;
        HashMap<String, int[]> intArrayTags;
        HashMap<String, Long> longTags;
        HashMap<String, long[]> longArrayTags;
        HashMap<String, Short> shortTags;
        HashMap<String, String> stringTags;

        boolean nameBoolean;
        Component name;
        if (jsonObject.has("name")) {
            name = MiniMessage.miniMessage().deserialize(jsonObject.get("name").getAsString());
            nameBoolean = true;
        } else {
            name = Component.text("");
            nameBoolean = false;
        }

        boolean loreBoolean;
        List<Component> lore;
        if (jsonObject.has("lore")) {
            lore = new ArrayList<>();
            jsonObject.get("lore").getAsJsonArray().forEach((element) -> {
                lore.add(MiniMessage.miniMessage().deserialize(element.getAsString()));
            });
            loreBoolean = true;
        } else {
            lore = new ArrayList<>();
            loreBoolean = false;
        }

        boolean cmdBoolean;
        int cmd;
        if (jsonObject.get("texture").getAsJsonObject().has("cmd")) {
            cmd = jsonObject.get("texture").getAsJsonObject().get("cmd").getAsInt();
            cmdBoolean = true;
        } else {
            cmd = 0;
            cmdBoolean = false;
        }

        intTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("int"), HashMap.class);
        booleanTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("boolean"), HashMap.class);
        byteTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("byte"), HashMap.class);
        byteArrayTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("byte_array"), HashMap.class);
        doubleTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("double"), HashMap.class);
        floatTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("float"), HashMap.class);
        intArrayTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("int_array"), HashMap.class);
        longTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("long"), HashMap.class);
        longArrayTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("long_array"), HashMap.class);
        shortTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("short"), HashMap.class);
        stringTags = gson.fromJson(jsonObject.get("tags").getAsJsonObject().get("string"), HashMap.class);

        NamespacedKey drop = NamespacedKey.fromString(jsonObject.get("place_block").getAsString().toLowerCase());

        Material material = CustomItem.getItemStack(NamespacedKey.fromString(jsonObject.get("texture").getAsJsonObject().get("material").getAsString())).getType();
        HashMap<String, String> commands = gson.fromJson(jsonObject.get("commands").getAsJsonObject(), HashMap.class);
        Class actions = Class.forName(jsonObject.get("actions_class").getAsString());

        CustomItem customItem = new CustomItem(material, key, actions);
        if (nameBoolean) {
            customItem.setName(name);
        }
        if (loreBoolean) {
            customItem.setLore(lore);
        }
        if (cmdBoolean) {
            customItem.setCMD(cmd);
        }

        customItem.setCommands(commands);
        customItem.setCustomBlock(drop);

        if (intTags != null) {
            intTags.forEach((k, v) -> customItem.addIntTag(k, v));
        }
        if (booleanTags != null) {
            booleanTags.forEach((k, v) -> customItem.addBooleanTag(k, v));
        }
        if (byteTags != null) {
            byteTags.forEach((k, v) -> customItem.addByteTag(k, v));
        }
        if (byteArrayTags != null) {
            byteArrayTags.forEach((k, v) -> customItem.addByteArrayTag(k, v));
        }
        if (doubleTags != null) {
            doubleTags.forEach((k, v) -> customItem.addDoubleTag(k, v));
        }
        if (floatTags != null) {
            floatTags.forEach((k, v) -> customItem.addFloatTag(k, v));
        }
        if (intArrayTags != null) {
            intArrayTags.forEach((k, v) -> customItem.addIntArrayTag(k, v));
        }
        if (longTags != null) {
            longTags.forEach((k, v) -> customItem.addLongTag(k, v));
        }
        if (longArrayTags != null) {
            longArrayTags.forEach((k, v) -> customItem.addLongArrayTag(k, v));
        }
        if (shortTags != null) {
            shortTags.forEach((k, v) -> customItem.addShortTag(k, v));
        }
        if (stringTags != null) {
            stringTags.forEach((k, v) -> customItem.addStringTag(k, v));
        }

        instances.put(key, customItem);
    }

    public static CustomItem fromNamespaceKey(NamespacedKey key) {
        if (!instances.containsKey(key)) {
            return null;
        }

        return instances.get(key);
    }

    public ItemStack getItemstack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "CItemVersion"), PersistentDataType.INTEGER, version);
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "CItem"), PersistentDataType.BOOLEAN, true);
        dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "CItemCodeName"), PersistentDataType.STRING, key.toString());
        if (customBlock != null) {
            dataContainer.set(new NamespacedKey(CustomItemApi.plugin, "CBlock"), PersistentDataType.STRING, customBlock.toString());
        }

        intTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.INTEGER, value));
        booleanTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.BOOLEAN, value));
        byteTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.BYTE, value));
        byteArrayTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.BYTE_ARRAY, value));
        doubleTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.DOUBLE, value));
        floatTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.FLOAT, value));
        intArrayTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.INTEGER_ARRAY, value));
        longTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.LONG, value));
        longArrayTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.LONG_ARRAY, value));
        shortTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.SHORT, value));
        stringTags.forEach((key, value) -> dataContainer.set(new NamespacedKey(CustomItemApi.plugin, key), PersistentDataType.STRING, value));

        meta.displayName(name);
        meta.lore(lore);
        meta.setCustomModelData(cmd);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public Class getActions() {
        return actions;
    }

    public CustomItem setActions(Class actions) {
        this.actions = actions;
        return this;
    }

    public CustomItem setMaterial(Material material) {
        this.material = material;
        version += 1;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public CustomItem setCMD(int cmd) {
        this.cmd = cmd;
        cmdBoolean = true;
        version += 1;
        return this;
    }

    public CustomItem setName(Component name) {
        this.name = name;
        nameBoolean = true;
        version += 1;
        return this;
    }

    public CustomItem setLore(List<Component> lore) {
        this.lore = lore;
        loreBoolean = true;
        version += 1;
        return this;
    }

    public CustomItem setCustomBlock(NamespacedKey customBlock) {
        this.customBlock = customBlock;
        version += 1;
        return this;
    }

    public CustomItem removeLore() {
        loreBoolean = false;
        version += 1;
        return this;
    }

    public CustomItem removeTag(String key) {
        if (!tagKeys.contains(key)) {
            return this;
        }

        tagKeys.remove(key);

        intTags.remove(key);
        booleanTags.remove(key);
        byteTags.remove(key);
        byteArrayTags.remove(key);
        doubleTags.remove(key);
        floatTags.remove(key);
        intArrayTags.remove(key);
        longTags.remove(key);
        longArrayTags.remove(key);
        shortTags.remove(key);
        stringTags.remove(key);

        return this;
    }

    public CustomItem removeCMD() {
        cmdBoolean = false;
        version += 1;
        return this;
    }

    public CustomItem removeName() {
        nameBoolean = false;
        version += 1;
        return this;
    }

    public CustomItem removeCustomBlock() {
        customBlock = null;
        version += 1;
        return this;
    }

    public NamespacedKey getCBlock() {
        return customBlock;
    }

    public HashMap<String, Integer> getIntTags() {
        return intTags;
    }

    public CustomItem addIntTag(String key, Integer value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        intTags.put(key, value);
        return this;
    }

    public HashMap<String, Boolean> getBooleanTags() {
        return booleanTags;
    }

    public CustomItem addBooleanTag(String key, Boolean value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        booleanTags.put(key, value);
        return this;
    }

    public HashMap<String, Byte> getByteTags() {
        return byteTags;
    }

    public CustomItem addByteTag(String key, Byte value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        byteTags.put(key, value);
        return this;
    }

    public HashMap<String, byte[]> getByteArrayTags() {
        return byteArrayTags;
    }

    public CustomItem addByteArrayTag(String key, byte[] value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        byteArrayTags.put(key, value);
        return this;
    }

    public HashMap<String, Double> getDoubleTags() {
        return doubleTags;
    }

    public CustomItem addDoubleTag(String key, Double value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        doubleTags.put(key, value);
        return this;
    }

    public HashMap<String, Float> getFloatTags() {
        return floatTags;
    }

    public CustomItem addFloatTag(String key, Float value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        floatTags.put(key, value);
        return this;
    }

    public HashMap<String, int[]> getIntArrayTags() {
        return intArrayTags;
    }

    public CustomItem addIntArrayTag(String key, int[] value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        intArrayTags.put(key, value);
        return this;
    }

    public HashMap<String, Long> getLongTags() {
        return longTags;
    }

    public CustomItem addLongTag(String key, Long value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        longTags.put(key, value);
        return this;
    }

    public HashMap<String, long[]> getLongArrayTags() {
        return longArrayTags;
    }

    public CustomItem addLongArrayTag(String key, long[] value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        longArrayTags.put(key, value);
        return this;
    }

    public HashMap<String, Short> getShortTags() {
        return shortTags;
    }

    public CustomItem addShortTag(String key, Short value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        shortTags.put(key, value);
        return this;
    }

    public HashMap<String, String> getStringTags() {
        return stringTags;
    }

    public CustomItem addStringTag(String key, String value) {
        if (tagKeys.contains(key)) {
            return this;
        }

        tagKeys.add(key);
        stringTags.put(key, value);
        return this;
    }

    public boolean getNameBoolean() {
        return nameBoolean;
    }

    public boolean getCmdBoolean() {
        return cmdBoolean;
    }

    public boolean getLoreBoolean() {
        return loreBoolean;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getTagKeys() {
        return tagKeys;
    }

    public int getCmd() {
        return cmd;
    }

    public Component getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public List<Component> getLore() {
        return lore;
    }

    public NamespacedKey getCustomBlock() {
        return customBlock;
    }

    public Boolean hasCustomBlock() {
        if (customBlock == null) {
            return false;
        }
        return true;
    }

    public static boolean isCustomItem(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta.getPersistentDataContainer().has(new NamespacedKey(CustomItemApi.plugin, "CItem"), PersistentDataType.BOOLEAN)) {
            return true;
        }

        return false;
    }

    public static boolean isCodeName(NamespacedKey codeName) {
        if (instances.containsKey(codeName)) {
            return true;
        }
        return false;
    }

    public static NamespacedKey getCodeName(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        Set<NamespacedKey> keys = dataContainer.getKeys();

        if (!keys.contains(new NamespacedKey(CustomItemApi.plugin, "CItem"))) {
            return null;
        }

        return NamespacedKey.fromString(dataContainer.get(new NamespacedKey(CustomItemApi.plugin, "CItemCodeName"), PersistentDataType.STRING));
    }

    /**
     * Will return an {@link ItemStack} with the material the vanilla material associated with
     * the {@link NamespacedKey} (similar to the give command).
     * Else, it will return an {@link ItemStack} with all the properties from {@link #getItemstack()}.
     * If there isn't a vanilla or custom item with this {@link NamespacedKey}, it will return <b>NULL</b>.
     */
    public static ItemStack getItemStack(NamespacedKey key) {
        Material material = Material.matchMaterial(key.toString());

        if (material != null) {
            return new ItemStack(material);
        }

        if (!instances.containsKey(key)) {
            return null;
        }

        return fromNamespaceKey(key).getItemstack();
    }
}

