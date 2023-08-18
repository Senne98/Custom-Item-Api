package org.super_man2006.custom_item_api.files;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Read {

    public static void read() {
        blocks();
        items();
    }

    private static void blocks() {
        File file = CustomItemApi.blocksFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if(!file.canRead()) {
            return;
        }

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            HashMap<String, String> blocks = gson.fromJson(reader, new TypeToken<HashMap<String, String>>(){}.getType());

            blocks.forEach((key, block) -> {
                try {
                    CustomItemApi.cBlockList.put(NamespacedKey.fromString(key), (CustomBlock) Base64.decode(block));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("Failed to load blocks");
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(e);
        }
    }

    private static void items() {
        File file = CustomItemApi.itemsFile;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if(!file.canRead()) {
            return;
        }

        try {
            JsonReader reader = new JsonReader(new FileReader(file));

            HashMap<String, String> items = gson.fromJson(reader, new TypeToken<HashMap<String, String>>(){}.getType());

            items.forEach((key, item) -> {
                try {
                    CustomItemApi.cItemList.put(NamespacedKey.fromString(key), (CustomItem) Base64.decode(item));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("Failed to load items");
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
