package com.gmail.goosius.townycultures.command;

import com.gmail.goosius.townycultures.Messaging;
import com.gmail.goosius.townycultures.enums.TownyCulturesPermissionNodes;
import com.gmail.goosius.townycultures.events.PreCultureSetEvent;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.Translation;
import com.gmail.goosius.townycultures.utils.CultureUtil;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CultureCommand implements CommandExecutor, TabCompleter {
	
	private static final List<String> cultureTabCompletes = Arrays.asList("set");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1)
			return NameUtil.filterByStart(cultureTabCompletes, args[0]);
		else
			return Collections.emptyList();
	}

	private void showCultureHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/culture"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/c set", "[culture]", ""));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player && args.length > 0)
			parseCultureCommand((Player) sender, args);
		else
			showCultureHelp(sender);

		return true;
	}

	private void parseCultureCommand(Player player, String[] args) {

		switch (args[0]) {
		case "set":
			parseCultureSetCommand(player, StringMgmt.remFirstArg(args));
			break;
		default:
			showCultureHelp(player);
		}
	}

	private void parseCultureSetCommand(Player player, String[] args) {

		if (args.length == 0) {
			showCultureHelp(player);
			return;
		}	
		
		if (!player.hasPermission(TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_SET_TOWN_CULTURE.getNode())) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
			return;
		}

		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if(!resident.hasTown())
			player.sendMessage(Translation.of("msg_err_command_disable"));

		Town town = TownyAPI.getInstance().getResidentTownOrNull(resident);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(args[0]);
		for(int i = 1; i < args.length; i++) {
			stringBuilder.append(" ").append(args[i]);
		}
		String newCulture = stringBuilder.toString();
		try {
			newCulture = CultureUtil.validateCultureName(newCulture);
		} catch (Exception e) {
			TownyMessaging.sendErrorMsg(player, e.getMessage());
		}
		if (newCulture == null) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_invalid_string_town_culture_not_set"));
		} else {
			
			//Fire cancellable event.
			PreCultureSetEvent event = new PreCultureSetEvent(newCulture, town);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				TownyMessaging.sendErrorMsg(player, event.getCancelMessage());
				return;
			}
			
			//Set town culture
			TownMetaDataController.setTownCulture(town, newCulture);
			if (newCulture.isEmpty())
				TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_culture_removed"));
			else
				TownyMessaging.sendPrefixedTownMessage(town, Translation.of("msg_town_culture_set", StringMgmt.capitalize(newCulture)));
		}
	}
}