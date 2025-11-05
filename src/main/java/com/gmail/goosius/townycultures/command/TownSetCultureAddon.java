package com.gmail.goosius.townycultures.command;

import com.gmail.goosius.townycultures.enums.TownyCulturesPermissionNodes;
import com.gmail.goosius.townycultures.events.PreCultureSetEvent;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.Settings;
import com.gmail.goosius.townycultures.utils.CultureUtil;
import com.gmail.goosius.townycultures.utils.Messaging;
import com.gmail.goosius.townycultures.utils.PresetCulturesUtil;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class TownSetCultureAddon extends BaseCommand implements TabExecutor {
	
	public TownSetCultureAddon() {
		AddonCommand townSetCultureCommand = new AddonCommand(CommandType.TOWN_SET, "culture", this);
		TownyCommandAddonAPI.addSubCommand(townSetCultureCommand);
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

	private void showCultureHelp(CommandSender sender) {
		Messaging.sendMessage(sender, ChatTools.formatTitle("/town set culture"));
		Messaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/t set culture", "[culturename]", "Set your culture."));
		Messaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/t set culture", "", "Remove your culture."));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player player) {
			try {
				parseSetCultureCommand(player, args);
			} catch (TownyException e) {
				Messaging.sendErrorMsg(player, e.getMessage(player));
			}
		} else
			showCultureHelp(sender);

		return true;
	}

	private void parseSetCultureCommand(Player player, String[] args) throws TownyException {
		checkPermOrThrow(player, TownyCulturesPermissionNodes.TOWNYCULTURES_COMMAND_SET_TOWN_CULTURE.getNode());

		Resident resident = getResidentOrThrow(player);
		Town town = getTownFromResidentOrThrow(resident);

		String newCultureName = CultureUtil.validateCultureName(StringMgmt.join(args, " "));

		if (newCultureName == null)
			throw new TownyException(Translatable.of("msg_err_invalid_string_town_culture_not_set"));

		//Fire cancellable event.
		BukkitTools.ifCancelledThenThrow(new PreCultureSetEvent(newCultureName, town));

        //If preset cultures is enabled, check that the given name is valid
        if(Settings.isPresetCulturesEnabled()) {
            if(newCultureName.isEmpty())
                throw new TownyException(Translatable.of("msg_err_culture_name_not_a_preset_culture", newCultureName, PresetCulturesUtil.getPresetCultureNamesAsSet().toString()));
            String correctlyCapitalizedCultureName = PresetCulturesUtil.findCorrectlyCapitalizedCultureName(newCultureName);
            if (correctlyCapitalizedCultureName == null)
                throw new TownyException(Translatable.of("msg_err_culture_name_not_a_preset_culture", newCultureName, PresetCulturesUtil.getPresetCultureNamesAsSet().toString()));
            else
                newCultureName = correctlyCapitalizedCultureName;
        }

		//Set town culture
		TownMetaDataController.setTownCulture(town, newCultureName);
		if (newCultureName.isEmpty())
			Messaging.sendPrefixedTownMessage(town, Translatable.of("msg_culture_removed"));
		else
			Messaging.sendPrefixedTownMessage(town, Translatable.of("msg_town_culture_set", newCultureName));
	}
}