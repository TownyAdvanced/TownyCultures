package com.townycultures.command;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.bukkit.util.NameValidation;
import com.palmergames.util.StringMgmt;
import com.palmergames.util.TimeMgmt;
import com.townycultures.Messaging;
import com.townycultures.enums.TownyCulturesPermissionNodes;
import com.townycultures.metadata.TownMetaDataController;
import com.townycultures.settings.Settings;
import com.townycultures.settings.Translation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CultureAdminCommand implements CommandExecutor, TabCompleter {

	private static final List<String> townyCulturesAdminTabCompletes = Arrays.asList("reload", "alltowns", "town");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		switch (args[0].toLowerCase()) {
			case "alltowns":
				if (args.length == 2)
					return NameUtil.filterByStart(Arrays.asList("set"), args[1]);

				if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("culture"), args[2]);

			case "town":
				if (args.length == 2)
					return getTownsStartingWith(args[1]);

				if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("set"), args[1]);

				if (args.length == 4)
					return NameUtil.filterByStart(Arrays.asList("culture"), args[2]);

			default:
				if (args.length == 1)
					return NameUtil.filterByStart(townyCulturesAdminTabCompletes, args[0]);
				else
					return Collections.emptyList();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		parseCultureAdminCommand(sender, args);
		return true;
	}

	private void parseCultureAdminCommand(CommandSender sender, String[] args) {

		//Todo - console access???
		if (sender instanceof Player && !(sender.hasPermission(TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_ADMIN.getNode(args[0])))) {
			Messaging.sendErrorMsg(sender, Translation.of("msg_err_command_disable"));
			return;
		}

		/*
		 * Parse Command.
		 */
		if (args.length > 0) {
			switch (args[0]) {
			case "reload":
				parseCultureAdminReloadCommand(sender);
				break;
			case "alltowns":
				parseCultureAdminAllTownsCommand(sender, StringMgmt.remFirstArg(args));
				break;
			case "town":
				parseCultureAdminTownCommand(sender, StringMgmt.remFirstArg(args));
				break;
			default:
				showHelp(sender);
			}
		} else {
			showHelp(sender);
		}
	}
	
	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/cultureadmin"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "reload", Translation.of("admin_help_1")));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "alltowns set culture [culture]", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "town [town_name] set culture [culture]", ""));
	}

	private void showCultureAdminTownHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "town [town_name] set culture [culture]", ""));
	}

	private void showCultureAdminAllTownsHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "alltowns set culture [culture]", ""));
	}

	private void parseCultureAdminReloadCommand(CommandSender sender) {
		if (Settings.loadSettingsAndLang()) {
			Messaging.sendMsg(sender, Translation.of("config_and_lang_file_reloaded_successfully"));
			return;
		}
		Messaging.sendErrorMsg(sender, Translation.of("config_and_lang_file_could_not_be_loaded"));
	}

	private void parseCultureAdminTownCommand(CommandSender sender, String[] args) {
		if (args.length == 4
			&& args[1].equalsIgnoreCase("set")
			&& args[2].equalsIgnoreCase("culture")) {

			Town town = TownyUniverse.getInstance().getTown(args[0]);
			if (town == null) {
				Messaging.sendErrorMsg(sender, Translation.of("msg_err_town_not_registered", args[0]));
				return;
			}

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[0]);
			for(int i = 1; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}
			String newCulture = stringBuilder.toString();

			if (!newCulture.equals("none")) {
				if (!NameValidation.isValidString(newCulture)) {
					String messageForAdmin = Translation.of("msg_err_invalid_string_town_culture_not_set");
					if(sender instanceof Player) {
						Messaging.sendErrorMsg(sender, messageForAdmin);
					} else {
						System.out.println(messageForAdmin);
					}
					return;
				}
				// TownyFormatter shouldn't be given any string longer than 159, or it has trouble splitting lines.
				if (newCulture.length() > 159)
					newCulture = newCulture.substring(0, 159);
			} else {
				newCulture = "";
			}

			//Set culture
			TownMetaDataController.setTownCulture(town, newCulture);
			TownyMessaging.sendPrefixedTownMessage(town, String.format(Translation.of("msg_town_culture_set"), newCulture));
			String messageForAdmin = String.format(Translation.of("msg_specific_town_cultures_set"), town.getName(), newCulture);
			if(sender instanceof Player) {
				Messaging.sendErrorMsg(sender, messageForAdmin);
			} else {
				System.out.println(messageForAdmin);
			}
		} else
			showCultureAdminTownHelp(sender);
	}

	private void parseCultureAdminAllTownsCommand(CommandSender sender, String[] args) {
		if (args.length == 3
				&& args[0].equalsIgnoreCase("set")
				&& args[1].equalsIgnoreCase("culture")) {

			String newCulture = args[2];

			if (!newCulture.equals("none")) {
				if (!NameValidation.isValidString(newCulture)) {
					String messageForAdmin = Translation.of("msg_err_invalid_string_town_culture_not_set");
					if(sender instanceof Player) {
						Messaging.sendErrorMsg(sender, messageForAdmin);
					} else {
						System.out.println(messageForAdmin);
					}
					return;
				}
				// TownyFormatter shouldn't be given any string longer than 159, or it has trouble splitting lines.
				if (newCulture.length() > 159)
					newCulture = newCulture.substring(0, 159);
			} else {
				newCulture = "";
			}

			//Set culture in all towns
			for(Town town: TownyUniverse.getInstance().getTowns()) {
				TownMetaDataController.setTownCulture(town, newCulture);
			}
			TownyMessaging.sendGlobalMessage(String.format(Translation.of("msg_all_town_cultures_set"), newCulture));
			String messageForAdmin = String.format(Translation.of("msg_all_town_cultures_set"), newCulture);
			if(sender instanceof Player) {
				Messaging.sendErrorMsg(sender, messageForAdmin);
			} else {
				System.out.println(messageForAdmin);
			}
		} else
			showCultureAdminAllTownsHelp(sender);
	}

	/**
	 * Returns a List<String> containing strings of town names that match with arg.
	 *
	 * @return Matches
	 */
	static List<String> getTownsStartingWith(String arg) {
		List<String> matches = new ArrayList<>();
		TownyUniverse townyUniverse = TownyUniverse.getInstance();
		matches.addAll(townyUniverse.getTownsTrie().getStringsFromKey(arg));
		return matches;
	}
}

