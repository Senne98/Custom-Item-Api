package org.super_man2006.custom_item_api.CustomItems.load;

import org.bukkit.NamespacedKey;
import org.super_man2006.custom_item_api.CustomItemApi;
import org.super_man2006.custom_item_api.CustomItems.blocks.CustomBlock;
import org.super_man2006.custom_item_api.CustomItems.blocks.TransparentCustomBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoadBlocks {

    static File packFolder = new File(CustomItemApi.plugin.getDataFolder(), "packs");

    public static void load() throws IOException, ClassNotFoundException {
        if (packFolder.exists()) {

            for (File namespaceFolder : packFolder.listFiles()) {
                if (namespaceFolder.isDirectory()) {

                    //blocks folder
                    for (File blockFolder : namespaceFolder.listFiles()) {
                        if (blockFolder.getName().equals("blocks") && blockFolder.isDirectory()) {



                            for (File subBlockFolder : blockFolder.listFiles()) {
                                //default block folder
                                if (subBlockFolder.getName().equals("default_block") && subBlockFolder.isDirectory()) {

                                    //load default block files
                                    for (File blockFiles : subBlockFolder.listFiles()) {
                                        if (blockFiles.getName().endsWith(".json") && blockFiles.isFile()) {
                                            CustomItemApi.plugin.getLogger().info("Loading block: " + namespaceFolder.getName().toLowerCase() + ":" + blockFiles.getName().replace(".json", "").toLowerCase());
                                            loadDefaultBlock(blockFiles, NamespacedKey.fromString(namespaceFolder.getName().toLowerCase() + ":" + blockFiles.getName().replace(".json", "").toLowerCase()));
                                        }
                                    }


                                //transparent block folder
                                } else if (subBlockFolder.getName().equals("transparent_block") && subBlockFolder.isDirectory()) {

                                    //load transparent block files
                                    for (File blockFiles : subBlockFolder.listFiles()) {
                                        if (blockFiles.getName().endsWith(".json") && blockFiles.isFile()) {
                                            CustomItemApi.plugin.getLogger().info("Loading transparent block: " + namespaceFolder.getName().toLowerCase() + ":" + blockFiles.getName().replace(".json", "").toLowerCase());
                                            loadTransparentBlock(blockFiles, NamespacedKey.fromString(namespaceFolder.getName().toLowerCase() + ":" + blockFiles.getName().replace(".json", "").toLowerCase()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void loadDefaultBlock(File file, NamespacedKey key) throws IOException, ClassNotFoundException {
        /*write code to parse json from this format to a CustomBlock object with Gson
        {
	        "texture": {
		        "material": "minecraft:item"
		        "cmd": {
			        "use_cmd": boolean
			        "cmd": int
			    }
		    }
	        "placement": {
		        "placed_block": "minecraft:block"
		        "rotation": "type"
	        }
	        "commands": {
                "click": "command"
                "place": "command"
                "break": "command"
	        }
	        "actions_class": "class"
	        "drop": "plugin/minecraft:item"
         }
         */


        CustomBlock.fromJson(new String(Files.readAllBytes(file.toPath())), key);

    }

    private static void loadTransparentBlock(File file, NamespacedKey key) throws IOException, ClassNotFoundException {
        /*write code to parse json from this format to a TransparentCustomBlock object with Gson
        {
	        "texture": {
		        "material": "minecraft:item"
		        "cmd": {
			        "use_cmd": boolean
			        "cmd": int
			    }
		    }
	        "placement": {
		        "placed_block": "minecraft:block"
		        "rotation": "type"
	        }
	        "commands": {
                "click": "command"
                "place": "command"
                "break": "command"
	        }
	        "actions_class": "class"
	        "drop": "plugin/minecraft:item"
         }
         */


        TransparentCustomBlock.fromJson(new String(Files.readAllBytes(file.toPath())), key);
    }

}
