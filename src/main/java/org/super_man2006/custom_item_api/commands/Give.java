package org.super_man2006.custom_item_api.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.super_man2006.custom_item_api.CustomItems.items.CustomItem;
import org.super_man2006.custom_item_api.utils.IsInt;

import java.util.List;

public class Give implements Listener {

    @EventHandler
    public void onGive(PlayerCommandPreprocessEvent e) {
        List<String> args = List.of(e.getMessage().toLowerCase().split(" "));

        if (!args.get(0).toLowerCase().equals("/give") && !args.get(0).toLowerCase().equals("/minecraft:give")) {
            return;
        }

        e.setCancelled(true);

        int size = args.size();

        if (size == 1) {
            e.getPlayer().sendMessage(Component.text("/give <target> <item> <amount>").color(NamedTextColor.RED));
        } else if (size == 4) {
            if (NamespacedKey.fromString(args.get(2)) != null && IsInt.IsInt(args.get(3))) {
                try {
                    Bukkit.selectEntities(e.getPlayer(), args.get(1)).forEach(entity ->
                    {
                        if (entity instanceof Player) {
                            ItemStack itemStack = CustomItem.getItemStack(NamespacedKey.fromString(args.get(2)));
                            itemStack.setAmount(Integer.parseInt(args.get(3)));
                            ((Player) entity).getInventory().addItem(itemStack);
                        }
                    });
                } catch (IllegalArgumentException exception) {
                    e.getPlayer().sendMessage(Component.text("invalid command!").color(NamedTextColor.RED));
                }
            }else {
                e.getPlayer().sendMessage(Component.text("invalid command!").color(NamedTextColor.RED));
            }

        } else if (size == 3) {
            if (NamespacedKey.fromString(args.get(2)) != null) {
                try {
                    Bukkit.selectEntities(e.getPlayer(), args.get(1)).forEach(entity ->
                    {
                        if (entity instanceof Player) {
                            ItemStack itemStack = CustomItem.getItemStack(NamespacedKey.fromString(args.get(2)));
                            ((Player) entity).getInventory().addItem(itemStack);
                        }
                    });
                } catch (IllegalArgumentException exception) {
                    e.getPlayer().sendMessage(Component.text("invalid command!").color(NamedTextColor.RED));
                }

            } else {
                e.getPlayer().sendMessage(Component.text("invalid command!").color(NamedTextColor.RED));
            }

        }
    }

    /*@EventHandler
    public void onTab(AsyncPlayerSendCommandsEvent e) {

        /*if (!(e.isAsynchronous() || !e.hasFiredAsync())) {
            return;
        }*/

        /*if (!e.getCommandNode().getName().equals("give")) {
            return;
        }

        RootCommandNode commandNode = e.getCommandNode();

        if (e.getCommandNode().getChildren().contains("give")) {
            Bukkit.getServer().sendMessage(Component.text("test"));
        }

        Bukkit.getServer().sendMessage(Component.text(e.getCommandNode().getChildren().size()));
        List<CommandNode> nodes = e.getCommandNode().getChildren().stream().toList();
        nodes.forEach(commandNode -> {
            if (commandNode.getName().equals("give")) {
                List<CommandNode> childNodes = commandNode.getChildren().stream().toList();
                List<CommandNode> blockNodes = childNodes.get(0).getChildren().stream().toList();
                Bukkit.getServer().sendMessage(Component.text(blockNodes.get(0).getName()));
            }
        });
    }*/
}
