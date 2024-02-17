package org.super_man2006.custom_item_api.CustomItems.load;

import org.bukkit.NamespacedKey;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.TransparentCustomBlock;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoadItems {

    static File packFolder = new File(CustomItemApi.plugin.getDataFolder(), "packs");

    public static void load() throws IOException, ClassNotFoundException {
        if (packFolder.exists()) {

            for (File namespaceFolder : packFolder.listFiles()) {
                if (namespaceFolder.isDirectory()) {

                    //blocks folder
                    for (File itemFolder : namespaceFolder.listFiles()) {
                        if (itemFolder.getName().equals("items") && itemFolder.isDirectory()) {

                            //load default block files
                            for (File itemFiles : itemFolder.listFiles()) {
                                if (itemFiles.getName().endsWith(".json") && itemFiles.isFile()) {
                                    System.out.println("Loading item: " + namespaceFolder.getName() + ":" + itemFiles.getName().replace(".json", ""));
                                    System.out.println(NamespacedKey.fromString(namespaceFolder.getName().toLowerCase() + ":" + itemFiles.getName().replace(".json", "").toLowerCase()).toString());
                                    loadItem(itemFiles, NamespacedKey.fromString(namespaceFolder.getName().toLowerCase() + ":" + itemFiles.getName().replace(".json", "").toLowerCase()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void loadItem(File file, NamespacedKey key) throws IOException, ClassNotFoundException {
        /*write code to parse json from this format to a CustomItem object with Gson
        {
            "texture": {
                "material": "minecraft:item"
                "cmd": {
                    "use_cmd": boolean
                    "cmd": int
                    }
            }
            "name": "string",
            "lore": {
                "String",
                "String",
                ...
            }
	        "tags": {
	            "int": [
	            ]
	            "boolean": [
                ]
                "byte": [
                ]
                "byte_array": [
                ]
                "double": [
                ]
                "float": [
                ]
                "int_array": [
                ]
                "long": [
                ]
                "long_array": [
                ]
                "short": [
                ]
                "string": [
                ]
	        }
	        "commands": {
		        "left_click": "command",
	        	"right_click": "command"
	        }
	        "actions_class": "class"
	        "place_block": "plugin/minecraft:item"
        }
         */


        CustomItem.fromJson(new String(Files.readAllBytes(file.toPath())), key);
    }
}
