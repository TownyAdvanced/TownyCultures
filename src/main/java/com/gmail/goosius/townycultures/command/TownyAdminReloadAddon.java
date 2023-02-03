package com.gmail.goosius.townycultures.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import com.gmail.goosius.townycultures.enums.TownyCulturesPermissionNodes;
import com.gmail.goosius.townycultures.settings.Settings;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.NoPermissionException;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Translatable;

public class TownyAdminReloadAddon extends BaseCommand implements TabExecutor {

	public TownyAdminReloadAddon() {
		AddonCommand townyAdminReloadCommand = new AddonCommand(CommandType.TOWNYADMIN_RELOAD, "townycultures", this);
		TownyCommandAddonAPI.addSubCommand(townyAdminReloadCommand);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			parseAdminReloadCommand(args, sender);
		} catch (NoPermissionException e) {
			TownyMessaging.sendErrorMsg(sender, e.getMessage(sender));
		}
		return true;
	}

	private void parseAdminReloadCommand(String[] args, CommandSender sender) throws NoPermissionException {
		checkPermOrThrow(sender, TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_ADMIN.getNode(args[0]));

		if (Settings.loadSettingsAndLang())
			TownyMessaging.sendMsg(sender, Translatable.of("config_and_lang_file_reloaded_successfully"));
		else
			TownyMessaging.sendErrorMsg(sender, Translatable.of("config_and_lang_file_could_not_be_loaded"));
	}
}
