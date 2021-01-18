package com.townycultures.command;

import com.townycultures.Messaging;
import com.townycultures.enums.TownyCulturesPermissionNodes;
import com.townycultures.metadata.TownMetaDataController;
import com.townycultures.settings.Translation;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.bukkit.util.NameValidation;
import com.palmergames.util.StringMgmt;
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

		if (!player.hasPermission(TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_SET_TOWN_CULTURE.getNode(args[0]))) {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
			return;
		}

		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return;

		if(!resident.hasTown())
			player.sendMessage(Translation.of("msg_err_command_disable"));

		try {
			Town town = resident.getTown();

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[0]);
			for(int i = 1; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}
			String newCulture = stringBuilder.toString();

			if (!newCulture.equals("none")) {
				if (!NameValidation.isValidString(newCulture)) {
					Messaging.sendErrorMsg(player, Translation.of("msg_err_invalid_string_town_culture_not_set"));
					return;
				}
				// TownyFormatter shouldn't be given any string longer than 159, or it has trouble splitting lines.
				if (newCulture.length() > 159)
					newCulture = newCulture.substring(0, 159);
			} else
				newCulture = "";

			//Set town culture
			TownMetaDataController.setTownCulture(town, newCulture);
			TownyMessaging.sendPrefixedTownMessage(town, String.format(Translation.of("msg_town_culture_set"), newCulture));
		} catch (NotRegisteredException e) {
			//We probably won't get here as we already checked for resident
		}
	}
}