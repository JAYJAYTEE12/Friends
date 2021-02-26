package org.jayjaytee.reego.friends.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jayjaytee.reego.friends.Friends;
import org.jayjaytee.reego.friends.utils.Messages;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class FriendCommand implements CommandExecutor {
    public HashMap<Player, Player> ongoingFriend = new HashMap<>();

    Friends plugin;

    public FriendCommand(Friends plugin) {
        this.plugin = plugin;
        plugin.getCommand("friend").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.Color("&cYou must be a player to execute that command!"));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§e---------------------------------");
            player.sendMessage("§e* §f/friend (player) §7Send a friend request to a player.");
            player.sendMessage("§e* §f/friend accept/decline (player) §7Accept or decline a incoming friend request.");
            player.sendMessage("§e* §f/friend remove (player) §7Removes a player from your friends list.");
            player.sendMessage("§e---------------------------------");
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cThat player isn't online!");
                return true;
            }
            if (plugin.getFriendsConfig().get(player.getUniqueId() + ".friends." + target.getUniqueId()) == null) {
                plugin.getFriendsConfig().set(player.getUniqueId() + ".friends." + target.getUniqueId(), false);
            }
            // Check for if target has ongoing with player
            if (ongoingFriend.containsKey(target)) {
                if (ongoingFriend.containsValue(player)) {
                    // Accept
                    player.sendMessage("§aYou tried to request but the target has already requested you!");
                    return true;
                }
            }

            // Check for if player has already sent a request to that player
            if (ongoingFriend.containsKey(player)) {
                if (ongoingFriend.containsValue(target)) {
                    player.sendMessage("§cYou already have an ongoing friend request to " + target.getName());
                    return true;
                }
            }

            boolean path = plugin.getFriendsConfig().getBoolean(player.getUniqueId() + ".friends." + target.getUniqueId());

            // Check if player is already friends
            // Note: If player's path of friends with target is true than target's path must be true.
            if (path) {
                player.sendMessage("§cYou are already friends with that person!");
                return true;
            }

            if (!path) {
                player.sendMessage("§aSuccessfully sent a friend request to " + target.getName());

                target.sendMessage("§e---------------------------------");
                target.sendMessage(player.displayName());
                target.sendMessage("§aWould like to be your friend! §eType /friend accept " + player.getName());

                TextComponent declineHover = Component.text()
                        .content("§7Click me to §c§lDECLINE")
                        .build();
                TextComponent acceptHover = Component.text()
                        .content("§7Click me to §a§lACCEPT")
                        .build();
                TextComponent decline = Component.text()
                        .content("  §c§l[DECLINE]")
                        .hoverEvent(declineHover.asHoverEvent())
                        .clickEvent(ClickEvent.runCommand("/friend decline " + player.getName()))
                        .build();
                TextComponent accept = Component.text()
                        .content("§a§l[ACCEPT]")
                        .hoverEvent(acceptHover.asHoverEvent())
                        .clickEvent(ClickEvent.runCommand("/friend accept " + player.getName()))
                        .append(decline)
                        .build();
                target.sendMessage(accept);

                target.sendMessage("§e---------------------------------");

                ongoingFriend.put(player, target);
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("accept")) {
                Player target = Bukkit.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cThat player isn't online!");
                    return true;
                }

                if (!ongoingFriend.containsKey(target)) {
                    if (!ongoingFriend.containsValue(player)) {
                        player.sendMessage("§cYou don't have any pending friend requests!");
                        return true;
                    }
                }
                
            }
        }
        return true;
    }
}
