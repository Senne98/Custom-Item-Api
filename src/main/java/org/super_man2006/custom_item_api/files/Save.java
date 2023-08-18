package org.super_man2006.custom_item_api.files;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.super_man2006.custom_item_api.CustomItemApi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Save {

    public static void save() {
        blocks();
        items();
    }

    private static void blocks() {
        File file = CustomItemApi.blocksFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (CustomItemApi.cBlockList == null) {
            return;
        }

        HashMap<String, String> stringHashMap = new HashMap<>();
        CustomItemApi.cBlockList.forEach((key, block) -> stringHashMap.put(key.asString(), Base64.encode(block)));

        try (JsonWriter writer = new JsonWriter(new FileWriter(file))){
            gson.toJson(gson.toJsonTree(stringHashMap, new TypeToken<HashMap<String , String>>(){}.getType()), writer);
            writer.flush();

        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save blocks");
            throw new RuntimeException(e);
        }
    }

    private static void items() {
        File file = CustomItemApi.itemsFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (CustomItemApi.cItemList == null) {
            return;
        }

        HashMap<String, String> stringHashMap = new HashMap<>();
        CustomItemApi.cItemList.forEach((key, item) -> stringHashMap.put(key.asString(), Base64.encode(item)));

        try (JsonWriter writer = new JsonWriter(new FileWriter(file))){

            gson.toJson(gson.toJsonTree(stringHashMap, new TypeToken<HashMap<String , String>>(){}.getType()), writer);
            writer.flush();

        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save items");
            throw new RuntimeException(e);
        }
    }
}
