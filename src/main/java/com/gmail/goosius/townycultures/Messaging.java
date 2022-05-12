package com.gmail.goosius.townycultures;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging {
	
	public static void sendErrorMsg(CommandSender sender, String message) {
		//Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
	        sender.sendMessage(Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.Red + message);
	}
	
	public static void sendErrorMsg(CommandSender sender, Translatable message) {
		//Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
	        sender.sendMessage(Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.Red + message.forLocale(sender));
	}

	public static void sendMsg(CommandSender sender, String message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
    		sender.sendMessage(Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.White + message);
	}
	
	public static void sendMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
    		sender.sendMessage(Translatable.of("cultures_plugin_prefix").forLocale(sender) + Colors.White + message.forLocale(sender));
	}
	
	public static void sendGlobalMessage(String message) {
        TownyCultures.info(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null && TownyAPI.getInstance().isTownyWorld(player.getWorld()))
            	sendMsg(player, message);
        }
	}
}
