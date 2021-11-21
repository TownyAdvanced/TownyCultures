package com.gmail.goosius.townycultures.command;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.BookFactory;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import com.gmail.goosius.townycultures.Messaging;
import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.enums.TownyCulturesPermissionNodes;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.Settings;
import com.gmail.goosius.townycultures.settings.Translation;
import com.gmail.goosius.townycultures.utils.CultureUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CultureAdminCommand implements CommandExecutor, TabCompleter {

	private static final List<String> townyCulturesAdminTabCompletes = Arrays.asList("reload", "alltowns", "town", "culturelist", "deleteculture");

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		switch (args[0].toLowerCase()) {
			case "alltowns":
				if (args.length == 2)
					return NameUtil.filterByStart(Arrays.asList("set"), args[1]);
				else if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("culture"), args[2]);
				else
					return Collections.emptyList();
			case "town":
				if (args.length == 2)
					return getTownsStartingWith(args[1]);
				else if (args.length == 3)
					return NameUtil.filterByStart(Arrays.asList("set"), args[2]);
				else if (args.length == 4)
					return NameUtil.filterByStart(Arrays.asList("culture"), args[3]);
				return Collections.emptyList();
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
		/*
		 * Parse Command.
		 */
		if (args.length > 0) {

			if (sender instanceof Player && !(sender.hasPermission(TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_ADMIN.getNode(args[0])))) {
				Messaging.sendErrorMsg(sender, Translation.of("msg_err_command_disable"));
				return;
			}

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
			case "culturelist":
				parseCACultureListCommand(sender);
				break;
			case "deleteculture":
				parseCADeleteCultureCommand(sender, StringMgmt.remFirstArg(args));
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
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "culturelist", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ca", "deleteculture [culturename]", ""));
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

	

	private void parseCADeleteCultureCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			Messaging.sendErrorMsg(sender, Translation.of("msg_err_not_enough_args", "/ca deletecultlure [culturename]"));
			return;
		}
		
		String culture = args[0];
		boolean found = false;
		for (Town town : new ArrayList<>(TownyUniverse.getInstance().getTowns())) {
			if (TownyCultures.getCulture(town).equalsIgnoreCase(culture)) {
				TownMetaDataController.setTownCulture(town, "");
				found = true;
			}
		}
		if (found)
			Messaging.sendMsg(sender, Translation.of("msg_culture_purged", culture));
		else 
			Messaging.sendErrorMsg(sender, Translation.of("msg_err_no_towns_found_with_this_culture", culture));
	}
	
	private void parseCACultureListCommand(CommandSender sender) {
		
		List<String> cultureList = gatherCultures();
		if (cultureList.isEmpty()) {
			Messaging.sendErrorMsg(sender, Translation.of("msg_err_no_cultures_found"));
			return;
		}
		
		if (sender instanceof ConsoleCommandSender) {
			TownyCultures.info(Translation.of("msg_cultures_found"));
			for (String culture : cultureList)
				TownyCultures.info(" * " + culture);
		} else if (sender instanceof Player) {
			Player player = (Player) sender;
			String booktext = Translation.of("msg_cultures_found");
			for (String culture : cultureList)
				booktext += "\n * " + culture;
			player.openBook(BookFactory.makeBook("Cultures", "Cultures", booktext));
		}
	}
	
	private List<String> gatherCultures() {
		List<String> cultureList = new ArrayList<>();
		for (Town town : new ArrayList<>(TownyUniverse.getInstance().getTowns())) {
			if (TownyCultures.hasCulture(town) && !cultureList.contains(TownyCultures.getCulture(town)))
				cultureList.add(TownyCultures.getCulture(town));
		}
		return cultureList;
	}

	private void parseCultureAdminTownCommand(CommandSender sender, String[] args) {
		if (args.length > 3
			&& args[1].equalsIgnoreCase("set")
			&& args[2].equalsIgnoreCase("culture")) {

			Town town = TownyUniverse.getInstance().getTown(args[0]);
			if (town == null) {
				Messaging.sendErrorMsg(sender, Translation.of("msg_err_town_not_registered", args[0]));
				return;
			}

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[3]);
			for(int i = 4; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}

			String newCulture;
			try {
				newCulture = CultureUtil.validateCultureName(stringBuilder.toString());
			} catch (Exception e) {
				if(sender instanceof Player)
					Messaging.sendErrorMsg(sender, e.getMessage());
				else
					TownyCultures.severe(e.getMessage());
				return;
			}

			//Set culture
			TownMetaDataController.setTownCulture(town, newCulture);
			
			//Prepare feedback message.
			String message= Translation.of("msg_culture_removed");
			if (!newCulture.isEmpty())
				message = Translation.of("msg_specific_town_cultures_set", town.getName(), StringMgmt.capitalize(newCulture));
			
			TownyMessaging.sendPrefixedTownMessage(town, message);

			if(sender instanceof Player)
				Messaging.sendMsg(sender, message);
			else
				TownyCultures.severe(message); // Send message to console.

		} else {
			showCultureAdminTownHelp(sender);
		}
	}

	private void parseCultureAdminAllTownsCommand(CommandSender sender, String[] args) {
		if (args.length > 2
				&& args[0].equalsIgnoreCase("set")
				&& args[1].equalsIgnoreCase("culture")) {

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(args[2]);
			for(int i = 3; i < args.length; i++) {
				stringBuilder.append(" ").append(args[i]);
			}

			String newCulture;
			try {
				newCulture = CultureUtil.validateCultureName(stringBuilder.toString());
			} catch (Exception e) {
				if(sender instanceof Player)
					Messaging.sendErrorMsg(sender, e.getMessage());
				else
					TownyCultures.severe(e.getMessage());
				return;
			}

			//Set culture in all towns
			for(Town town: TownyUniverse.getInstance().getTowns()) {
				TownMetaDataController.setTownCulture(town, newCulture);
			}

			//Prepare feedback message.
			String message = Translation.of("msg_culture_removed_all_towns");
			if (!newCulture.isEmpty())
				message = Translation.of("msg_all_town_cultures_set", StringMgmt.capitalize(newCulture));
			
			Messaging.sendGlobalMessage(message);
			
			if(!(sender instanceof Player)) // Send message to console.
				TownyCultures.info(message);

		} else {
			showCultureAdminAllTownsHelp(sender);
		}
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

