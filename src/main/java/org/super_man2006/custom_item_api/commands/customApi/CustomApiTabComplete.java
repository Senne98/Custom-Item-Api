package org.super_man2006.custom_item_api.commands.customApi;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Nullable;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomApiTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("customapi")) {
            if (args.length == 1) {
                List<String> returnList = new ArrayList<>();
                if (sender.hasPermission("customitemapi.op") || sender instanceof ConsoleCommandSender) {
                    returnList.add("permission");
                    returnList.add("help");
                    returnList.add("give");
                } else if (sender.hasPermission("customitemapi.givecmd")) {
                    returnList.add("give");
                }
                List<String> finalReturnList = new ArrayList<>();
                returnList.forEach(s -> {
                    if (s.startsWith(args[0])) {
                        finalReturnList.add(s);
                    }
                });
                return finalReturnList;
            }

            if (args.length == 2) {

                if (args[0].equalsIgnoreCase("permission")) {
                    if (sender.hasPermission("customitemapi.op") || sender instanceof ConsoleCommandSender) {
                        List<String> returnList = new ArrayList<>();
                        returnList.add("give");
                        returnList.add("remove");
                        List<String> finalReturnList = new ArrayList<>();
                        returnList.forEach(s -> {
                            if (s.startsWith(args[1])) {
                                finalReturnList.add(s);
                            }
                        });
                        return finalReturnList;
                    }
                }

                if (args[0].equalsIgnoreCase("help")) {
                    List<String> returnList = new ArrayList<>();
                    returnList.add("filestructure");
                    returnList.add("blockjson");
                    returnList.add("itemjson");
                    returnList.add("ask");
                    List<String> finalReturnList = new ArrayList<>();
                    returnList.forEach(s -> {
                        if (s.startsWith(args[1])) {
                            finalReturnList.add(s);
                        }
                    });
                    return finalReturnList;
                }
            }

            if (args.length == 3) {

                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("customitemapi.op") || sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op")) {
                        List<String> returnList = new ArrayList<>();
                        CustomItem.instances.forEach((namespacedKey, customItem) -> returnList.add(namespacedKey.toString()));
                        Material[] materials = Material.values();
                        Arrays.stream(materials).forEach(material -> returnList.add("minecraft:" + material.toString().toLowerCase()));
                        List<String> finalReturnList = new ArrayList<>();
                        returnList.forEach(s -> {
                            if (s.contains(args[2])) {
                                finalReturnList.add(s);
                            }
                        });
                        return finalReturnList;
                    }
                }
            }

            if (args.length == 4) {

                if (args[0].equalsIgnoreCase("permission")) {
                    if (sender.hasPermission("customitemapi.op") || sender instanceof ConsoleCommandSender) {
                        List<String> returnList = new ArrayList<>();
                        returnList.add("op");
                        returnList.add("givecmd");
                        List<String> finalReturnList = new ArrayList<>();
                        returnList.forEach(s -> {
                            if (s.startsWith(args[3])) {
                                finalReturnList.add(s);
                            }
                        });
                        return finalReturnList;
                    }
                }

                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("customitemapi.op") || sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op")) {
                        return new ArrayList<>();
                    }
                }
            }
        }
        return null;
    }
}
