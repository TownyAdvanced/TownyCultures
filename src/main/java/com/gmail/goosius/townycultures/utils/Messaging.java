package com.gmail.goosius.townycultures.utils;

import com.gmail.goosius.townycultures.TownyCultures;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging {

	public static void sendErrorMsg(CommandSender sender, String message) {
		// Ensure the sender is not null (i.e. is an online player who is not an npc)
		if (sender != null)
			TownyMessaging.sendMessage(sender, Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.Red + message);
	}

	public static void sendErrorMsg(CommandSender sender, Translatable message) {
		// Ensure the sender is not null (i.e. is an online player who is not an npc)
		if (sender != null)
			TownyMessaging.sendMessage(sender, Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.Red + message.forLocale(sender));
	}

	public static void sendMsg(CommandSender sender, Translatable message) {
		// Ensure the sender is not null (i.e. is an online player who is not an npc)
		if (sender != null)
			TownyMessaging.sendMessage(sender, Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.White + message.forLocale(sender));
	}

	public static void sendGlobalMessage(Translatable message) {
		TownyCultures.info(message.defaultLocale());
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != null && TownyAPI.getInstance().isTownyWorld(player.getWorld()))
				sendMsg(player, message);
		}
	}

	public static void sendPrefixedTownMessage(Town town, Translatable message) {
		TownyMessaging.sendPrefixedTownMessage(town, message);
	}

	/**
	 * Send a message with no prefix prepended.
	 * @param player Player to show the message to.
	 * @param message Translatable to display as a message.
	 */
	public static void sendMessage(CommandSender sender, String message) {
		TownyMessaging.sendMessage(sender, message);
	}

	/**
	 * Send a message with no prefix prepended.
	 * @param player Player to show the message to.
	 * @param message Translatable to display as a message.
	 */
	public static void sendMessage(CommandSender sender, Translatable message) {
		TownyMessaging.sendMessage(sender, message.forLocale(sender));
	}
}
