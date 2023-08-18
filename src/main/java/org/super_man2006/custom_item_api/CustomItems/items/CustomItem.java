package org.super_man2006.custom_item_api.CustomItems.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.super_man2006.custom_item_api.CustomItemApi;

import java.util.*;

public class CustomItem implements ConfigurationSerializable {

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
    //String codeName;
    List<Component> lore;
    NamespacedKey customBlock;

    public CustomItem(Material material, NamespacedKey key) {
        this.material = material;
        nameBoolean = false;
        cmdBoolean = false;
        version = 0;
        this.key = key;

        CustomItemApi.cItemList.put(key, this);
    }

    public CustomItem(NamespacedKey key) {
        if (!CustomItemApi.cItemList.containsKey(key)) {
            return;
        }

        this.nameBoolean = CustomItemApi.cItemList.get(key).getNameBoolean();
        this.cmdBoolean = CustomItemApi.cItemList.get(key).getCmdBoolean();
        this.loreBoolean = CustomItemApi.cItemList.get(key).getLoreBoolean();
        this.material = CustomItemApi.cItemList.get(key).material;
        this.intTags = CustomItemApi.cItemList.get(key).getIntTags();
        this.booleanTags = CustomItemApi.cItemList.get(key).getBooleanTags();
        this.byteTags = CustomItemApi.cItemList.get(key).getByteTags();
        this.byteArrayTags = CustomItemApi.cItemList.get(key).getByteArrayTags();
        this.doubleTags = CustomItemApi.cItemList.get(key).getDoubleTags();
        this.floatTags = CustomItemApi.cItemList.get(key).getFloatTags();
        this.intArrayTags = CustomItemApi.cItemList.get(key).getIntArrayTags();
        this.longTags = CustomItemApi.cItemList.get(key).getLongTags();
        this.longArrayTags = CustomItemApi.cItemList.get(key).getLongArrayTags();
        this.shortTags = CustomItemApi.cItemList.get(key).getShortTags();
        this.stringTags = CustomItemApi.cItemList.get(key).getStringTags();
        this.tagKeys = CustomItemApi.cItemList.get(key).getTagKeys();
        this.cmd = CustomItemApi.cItemList.get(key).getCmd();
        this.name = CustomItemApi.cItemList.get(key).getName();
        this.version = CustomItemApi.cItemList.get(key).getVersion();
        this.key = CustomItemApi.cItemList.get(key).getKey();
        this.lore = CustomItemApi.cItemList.get(key).getLore();
        this.customBlock = CustomItemApi.cItemList.get(key).getCustomBlock();
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

    public void setMaterial(Material material) {
        this.material = material;
        version += 1;
    }

    public int getVersion() {
        return version;
    }

    public void setCMD(int cmd) {
        this.cmd = cmd;
        cmdBoolean = true;
        version += 1;
    }

    public void setName(Component name) {
        this.name = name;
        nameBoolean = true;
        version += 1;
    }

    public void setLore(List<Component> lore) {
        this.lore = lore;
        loreBoolean = true;
        version += 1;
    }

    public void setCustomBlock(NamespacedKey customBlock) {
        this.customBlock = customBlock;
        version += 1;
    }

    public void removeLore() {
        loreBoolean = false;
        version += 1;
    }

    public void removeTag(String key) {
        if (!tagKeys.contains(key)) {
            return;
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

    }

    public void removeCMD() {
        cmdBoolean = false;
        version += 1;
    }

    public void removeName() {
        nameBoolean = false;
        version += 1;
    }

    public void removeCustomBlock() {
        customBlock = null;
        version += 1;
    }

    public NamespacedKey getCBlock() {
        return customBlock;
    }

    public HashMap<String, Integer> getIntTags() {
        return intTags;
    }

    public void addIntTag(String key, Integer value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        intTags.put(key, value);
    }

    public HashMap<String, Boolean> getBooleanTags() {
        return booleanTags;
    }

    public void addBooleanTag(String key, Boolean value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        booleanTags.put(key, value);
    }

    public HashMap<String, Byte> getByteTags() {
        return byteTags;
    }

    public void addByteTag(String key, Byte value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        byteTags.put(key, value);
    }

    public HashMap<String, byte[]> getByteArrayTags() {
        return byteArrayTags;
    }

    public void addByteArrayTag(String key, byte[] value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        byteArrayTags.put(key, value);
    }

    public HashMap<String, Double> getDoubleTags() {
        return doubleTags;
    }

    public void addDoubleTag(String key, Double value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        doubleTags.put(key, value);
    }

    public HashMap<String, Float> getFloatTags() {
        return floatTags;
    }

    public void addFloatTag(String key, Float value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        floatTags.put(key, value);
    }

    public HashMap<String, int[]> getIntArrayTags() {
        return intArrayTags;
    }

    public void addIntArrayTag(String key, int[] value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        intArrayTags.put(key, value);
    }

    public HashMap<String, Long> getLongTags() {
        return longTags;
    }

    public void addLongTag(String key, Long value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        longTags.put(key, value);
    }

    public HashMap<String, long[]> getLongArrayTags() {
        return longArrayTags;
    }

    public void addLongArrayTag(String key, long[] value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        longArrayTags.put(key, value);
    }

    public HashMap<String, Short> getShortTags() {
        return shortTags;
    }

    public void addShortTag(String key, Short value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        shortTags.put(key, value);
    }

    public HashMap<String, String> getStringTags() {
        return stringTags;
    }

    public void addStringTag(String key, String value) {
        if (tagKeys.contains(key)) {
            return;
        }

        tagKeys.add(key);
        stringTags.put(key, value);
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
        if (CustomItemApi.cItemList.containsKey(codeName)) {
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

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("nameBoolean", nameBoolean);
        data.put("cmdBoolean", cmdBoolean);
        data.put("loreBoolean", loreBoolean);
        data.put("material", material);
        data.put("intTags", intTags);
        data.put("booleanTags", booleanTags);
        data.put("byteTags", byteTags);
        data.put("byteArrayTags", byteArrayTags);
        data.put("doubleTags", doubleTags);
        data.put("floatTags", floatTags);
        data.put("intArrayTags", intArrayTags);
        data.put("longTags", longTags);
        data.put("longArrayTags", longArrayTags);
        data.put("shortTags", shortTags);
        data.put("stringTags", stringTags);
        data.put("tagKeys", tagKeys);
        data.put("cmd", cmd);
        data.put("name", GsonComponentSerializer.gson().serialize(name));
        data.put("version", version);
        data.put("key", key.asString());
        if (loreBoolean) {
            data.put("lore", lore);
        }
        data.put("customBlock", customBlock.asString());
        return data;
    }

    public CustomItem(@NotNull Map<String, Object> data) {
        nameBoolean = (Boolean) data.get("nameBoolean");
        cmdBoolean = (Boolean) data.get("cmdBoolean");
        loreBoolean = (Boolean) data.get("loreBoolean");
        material = (Material) data.get("material");
        intTags = (HashMap<String, Integer>) data.get("intTags");
        booleanTags = (HashMap<String, Boolean>) data.get("booleanTags");
        byteTags = (HashMap<String, Byte>) data.get("byteTags");
        byteArrayTags = (HashMap<String, byte[]>) data.get("byteArrayTags");
        doubleTags = (HashMap<String, Double>) data.get("doubleTags");
        floatTags = (HashMap<String, Float>) data.get("floatTags");
        intArrayTags = (HashMap<String, int[]>) data.get("intArrayTags");
        longTags = (HashMap<String, Long>) data.get("longTags");
        longArrayTags = (HashMap<String, long[]>) data.get("longArrayTags");
        shortTags = (HashMap<String, Short>) data.get("shortTags");
        stringTags = (HashMap<String, String>) data.get("stringTags");
        tagKeys = (List<String>) data.get("tagKeys");
        cmd = (int) data.get("cmd");
        name = GsonComponentSerializer.gson().deserialize((String) data.get("name"));
        version = (int) data.get("version");
        key = NamespacedKey.fromString((String) data.get("key"));
        if (loreBoolean) {
            lore = (List<Component>) data.get("lore");
        }
        customBlock = NamespacedKey.fromString((String) data.get("customBlock"));
    }
}

