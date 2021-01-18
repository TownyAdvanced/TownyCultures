package com.gmail.goosius.siegewar.command;

import com.gmail.goosius.siegewar.metadata.TownMetaDataController;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.ChatTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class CultureCommunicationCommand implements CommandExecutor, TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

	private void showCultureCommunicationHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/cc [msg]"));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player && args.length > 0)
			parseCultureCommunicationCommand((Player) sender, args);
		else
			showCultureCommunicationHelp(sender);

		return true;
	}

	/**
	 * Send message to any online players in culture
	 */
	private void parseCultureCommunicationCommand(Player player, String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for(String messagePart: args) {
			stringBuilder.append(" ").append(messagePart);
		}
		String unformattedMessage = stringBuilder.toString();
		String message = unformattedMessage;

		//Ensure the sender is in a town
		String townCulture = "";
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		if(resident != null && resident.hasTown()) {
			try{
				townCulture = TownMetaDataController.getTownCulture(resident.getTown());
			} catch (NotRegisteredException e) {
				return; //Player town not found
			}
		}

		Resident otherResident;
		for(Player otherPlayer: BukkitTools.getOnlinePlayers()) {
			try {
				otherResident = TownyUniverse.getInstance().getResident(otherPlayer.getUniqueId());
				if (otherResident != null
						&& otherResident.hasTown()
						&& TownMetaDataController.getTownCulture(otherResident.getTown()).equalsIgnoreCase(townCulture)) {

					//Send message
					TownyMessaging.sendMsg(otherPlayer, message);
				}

			} catch (NotRegisteredException nre) {
				//Player town not found
			}
		}
	}
}