package com.gmail.goosius.townycultures.command;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.palmergames.bukkit.util.BookFactory;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.enums.TownyCulturesPermissionNodes;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.utils.CultureUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CultureAdminCommand extends BaseCommand implements TabExecutor {

	private static final List<String> townyCulturesAdminTabCompletes = Arrays.asList("alltowns", "town", "culturelist", "deleteculture");

	public CultureAdminCommand() {
		AddonCommand townyAdminCultureCommand = new AddonCommand(CommandType.TOWNYADMIN, "culture", this);
		TownyCommandAddonAPI.addSubCommand(townyAdminCultureCommand);
	}

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
					return BaseCommand.getTownyStartingWith(args[1], "t");
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
		try {
			parseCultureAdminCommand(sender, args);
		} catch (TownyException e) {
			TownyMessaging.sendErrorMsg(sender, e.getMessage(sender));
		}
		return true;
	}

	private void parseCultureAdminCommand(CommandSender sender, String[] args) throws TownyException {
		/*
		 * Parse Command.
		 */
		if (args.length > 0) {
			checkPermOrThrow(sender, TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_ADMIN.getNode(args[0]));

			switch (args[0].toLowerCase(Locale.ROOT)) {
			case "alltowns" -> parseAllTownsCommand(sender, StringMgmt.remFirstArg(args));
			case "town" -> parseTownCommand(sender, StringMgmt.remFirstArg(args));
			case "culturelist" -> parseCultureListCommand(sender);
			case "deleteculture" -> parseDeleteCultureCommand(sender, StringMgmt.remFirstArg(args));
			default -> showHelp(sender);
			}
		} else {
			showHelp(sender);
		}
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatTitle("/townyadmin culture"));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "alltowns set culture [culture]", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "town [town_name] set culture [culture]", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "culturelist", ""));
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "deleteculture [culturename]", ""));
	}

	private void showCultureAdminTownHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "town [town_name] set culture [culture]", ""));
	}

	private void showCultureAdminAllTownsHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/ta culture", "alltowns set culture [culture]", ""));
	}

	private void parseDeleteCultureCommand(CommandSender sender, String[] args) throws TownyException {
		if (args.length == 0)
			throw new TownyException(Translatable.of("msg_err_not_enough_args", "/ta culture deleteculture [culturename]"));

		String culture = StringMgmt.join(args, " ");
		boolean found = false;
		for (Town town : new ArrayList<>(TownyUniverse.getInstance().getTowns())) {
			if (TownyCultures.getCulture(town).equalsIgnoreCase(culture)) {
				TownMetaDataController.setTownCulture(town, "");
				found = true;
			}
		}
		if (found)
			TownyMessaging.sendMsg(sender, Translatable.of("msg_culture_purged", culture));
		else 
			TownyMessaging.sendErrorMsg(sender, Translatable.of("msg_err_no_towns_found_with_this_culture", culture));
	}
	
	private void parseCultureListCommand(CommandSender sender) throws TownyException {
		List<String> cultureList = gatherCultures();
		if (cultureList.isEmpty())
			throw new TownyException(Translatable.of("msg_err_no_cultures_found"));

		if (sender instanceof ConsoleCommandSender) {
			TownyCultures.info(Translatable.of("msg_cultures_found").forLocale(sender));
			for (String culture : cultureList)
				TownyCultures.info(" * " + culture);
		} else if (sender instanceof Player) {
			Player player = (Player) sender;
			String booktext = Translatable.of("msg_cultures_found").forLocale(sender);
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

	private void parseTownCommand(CommandSender sender, String[] args) throws TownyException {
		if (args.length > 3
			&& args[1].equalsIgnoreCase("set")
			&& args[2].equalsIgnoreCase("culture")) {

			Town town = getTownOrThrow(args[0]);
			String newCulture = CultureUtil.validateCultureName(StringMgmt.join(StringMgmt.remArgs(args, 2), " "));

			//Set culture
			TownMetaDataController.setTownCulture(town, newCulture);

			//Prepare feedback message.
			Translatable message = newCulture.isEmpty() ? Translatable.of("msg_culture_removed")
				: Translatable.of("msg_specific_town_cultures_set", town.getName(), StringMgmt.capitalize(newCulture));

			TownyMessaging.sendPrefixedTownMessage(town, message);
			if (sender instanceof Player)
				TownyMessaging.sendErrorMsg(sender, message);

		} else {
			showCultureAdminTownHelp(sender);
		}
	}

	private void parseAllTownsCommand(CommandSender sender, String[] args) throws TownyException {
		if (args.length > 2
				&& args[0].equalsIgnoreCase("set")
				&& args[1].equalsIgnoreCase("culture")) {

			String newCulture = CultureUtil.validateCultureName(StringMgmt.join(StringMgmt.remArgs(args, 1), " "));

			//Set culture in all towns
			for(Town town: new ArrayList<>(TownyUniverse.getInstance().getTowns()))
				TownMetaDataController.setTownCulture(town, newCulture);

			//Prepare feedback message.
			Translatable message = newCulture.isEmpty() ? Translatable.of("msg_culture_removed_all_towns")
				: Translatable.of("msg_all_town_cultures_set", StringMgmt.capitalize(newCulture));

			TownyMessaging.sendGlobalMessage(message);

		} else {
			showCultureAdminAllTownsHelp(sender);
		}
	}

}

