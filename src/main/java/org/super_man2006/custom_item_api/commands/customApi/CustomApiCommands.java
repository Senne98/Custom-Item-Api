package org.super_man2006.custom_item_api.commands.customApi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.permissions.Permissions;
import org.super_man2006.custom_item_api.utils.IsInt;

public class CustomApiCommands implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CustomApi")) {
            if (args.length == 4 && args[0].equalsIgnoreCase("permission")) {

                if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op"))) {
                    sender.sendMessage(Component.text("You don't have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }

                Player player = Bukkit.getPlayer(args[2]);

                if (player == null) {
                    sender.sendMessage(Component.text("Player not found").color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("give")) {
                    if (args[3].equalsIgnoreCase("op")) {
                        Permissions.giveCustomApiOp(player);

                        sender.sendMessage(Component.text("Added ").append(player.displayName().append(Component.text(" to the op list"))).color(NamedTextColor.GREEN));
                        return true;
                    }

                    if (args[3].equalsIgnoreCase("givecmd")) {
                        Permissions.giveCustomApiGiveCmd(player);

                        sender.sendMessage(Component.text("Added ").append(player.displayName().append(Component.text(" to the givecmd list"))).color(NamedTextColor.GREEN));
                        return true;
                    }
                }

                if (args[1].equalsIgnoreCase("remove")) {
                    if (args[3].equalsIgnoreCase("op")) {
                        Permissions.removeCustomApiOp(player);

                        sender.sendMessage(Component.text("Removed ").append(player.displayName().append(Component.text(" from the op list"))).color(NamedTextColor.RED));
                        return true;
                    }

                    if (args[3].equalsIgnoreCase("givecmd")) {
                        Permissions.removeCustomApiGiveCmd(player);

                        sender.sendMessage(Component.text("Removed ").append(player.displayName().append(Component.text(" from the givecmd list"))).color(NamedTextColor.RED));
                        return true;
                    }
                }

                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {

                if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op"))) {
                    sender.sendMessage(Component.text("You don't have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }

                sender.sendMessage(Component.text("===== CustomItemAPI help =====").color(NamedTextColor.DARK_AQUA));
                sender.sendMessage(Component.text(" \u25CF ").append(Component.text("file structure").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/customapi help filestructure")));
                sender.sendMessage(Component.text(" \u25CF ").append(Component.text("block.json").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/customapi help blockjson")));
                sender.sendMessage(Component.text(" \u25CF ").append(Component.text("item.json").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/customapi help itemjson")));
                sender.sendMessage(Component.text(" \u25CF ").append(Component.text("permissions").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/customapi help permissions")));
                sender.sendMessage(Component.text(" \u25CF ").append(Component.text("ask question/... on github or discord").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/customapi help ask")));

                return true;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("help")) {

                if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op"))) {
                    sender.sendMessage(Component.text("You don't have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("fileStructure")) {
                    sender.sendMessage(Component.text("======= file structure =======").color(NamedTextColor.DARK_AQUA));
                    sender.sendMessage(Component.text(" \u221F <Namespace>").color(NamedTextColor.RED));
                    sender.sendMessage(Component.text("   \u221F blocks").color(NamedTextColor.RED));
                    sender.sendMessage(Component.text("     \u221F normal blocks").color(NamedTextColor.RED));
                    sender.sendMessage(Component.text("     - transparent blocks").color(NamedTextColor.RED));
                    sender.sendMessage(Component.text("   - items").color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("blockJson")) {
                    sender.sendMessage(Component.text("======== block.json =========").color(NamedTextColor.DARK_AQUA));
                    sender.sendMessage(Component.text(" {\n" +
                            "   texture\": {\n" +
                            "     material\": \"minecraft:item\"\n" +
                            "     cmd\": {\n" +
                            "       use_cmd\": boolean\n" +
                            "       cmd\": int\n" +
                            "     }\n" +
                            "   }\n" +
                            "   placement\": {\n" +
                            "     placed_block\": \"minecraft:block\"\n" +
                            "     rotation\": \"type\"\n" +
                            "   }\n" +
                            "   commands\": {\n" +
                            "     click\": \"command\"\n" +
                            "     place\": \"command\"\n" +
                            "     break\": \"command\"\n" +
                            "   }\n" +
                            "   actions_class\": \"class\"\n" +
                            "   drop\": \"plugin/minecraft:item\"\n" +
                            " }").color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("itemJson")) {
                    sender.sendMessage(Component.text("========= item.json =========").color(NamedTextColor.DARK_AQUA));
                    sender.sendMessage(Component.text("WIP!").color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("permissions")) {
                    sender.sendMessage(Component.text("======== permissions ========").color(NamedTextColor.DARK_AQUA));
                    sender.sendMessage(Component.text(" usage: ").append(Component.text("/customapi permission [give/remove] <player> <permission>")).color(NamedTextColor.RED));
                    sender.sendMessage(Component.text(" op: ").append(Component.text("gives access to all Custom_Item_API commands")).color(NamedTextColor.RED));
                    sender.sendMessage(Component.text(" givecmd: ").append(Component.text("gives access to the give command (/customapi give)")).color(NamedTextColor.RED));
                    return true;
                }

                if (args[1].equalsIgnoreCase("ask")) {
                    sender.sendMessage(Component.text("====== github/discord ======").color(NamedTextColor.DARK_AQUA));
                    sender.sendMessage(Component.text(" \u25CF ").append(Component.text("GitHub").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Senne98/Custom-Item-Api")));
                    sender.sendMessage(Component.text(" \u25CF ").append(Component.text("Discord").decoration(TextDecoration.UNDERLINED, true)).color(NamedTextColor.RED).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/DBabnRZAhC")));
                    return true;
                }
            }

            if ((args.length == 3 || args.length == 4) && args[0].equalsIgnoreCase("give")) {
                if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("customitemapi.op" ) || sender.hasPermission("customitemapi.givecmd"))) {
                    sender.sendMessage(Component.text("You don't have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    sender.sendMessage(Component.text("Player not found").color(NamedTextColor.RED));
                    return true;
                }

                int amount = 1;
                if (args.length == 4) {
                    if (IsInt.IsInt(args[3])) {
                        amount = Integer.parseInt(args[3]);
                    } else {
                        sender.sendMessage(Component.text("Amount must be a number").color(NamedTextColor.RED));
                        return true;
                    }
                }

                ItemStack itemStack = CustomItem.getItemStack(NamespacedKey.fromString(args[2]));
                if (itemStack == null) {
                    sender.sendMessage(Component.text("Block not found").color(NamedTextColor.RED));
                    return true;
                }

                itemStack.setAmount(amount);
                player.getInventory().addItem(itemStack);

            }

            return true;
        }
        return true;
    }
}
