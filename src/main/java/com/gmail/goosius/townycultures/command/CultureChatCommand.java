package com.gmail.goosius.townycultures.command;

import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import com.gmail.goosius.townycultures.utils.CultureUtil;
import com.gmail.goosius.townycultures.utils.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class CultureChatCommand extends BaseCommand implements TabExecutor {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

	private void showCultureCommunicationHelp(CommandSender sender) {
		Messaging.sendMessage(sender, ChatTools.formatCommand("Eg", "/cc", "[msg]", ""));
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			Player player = catchConsole(sender);
			if (args.length > 0)
				parseCultureCommunicationCommand(player, args);
			else
				showCultureCommunicationHelp(player);
		} catch (TownyException e) {
			Messaging.sendErrorMsg(sender, e.getMessage(sender));
		}

		return true;
	}

	/**
	 * Send message to any online players in culture
	 */
	private void parseCultureCommunicationCommand(Player player, String[] args) throws TownyException {
		//Ensure the sender has a Culture.
		String townCulture = getTownCultureOrThrow(player);
		Translatable message = Translatable.of("culture_chat_message", townCulture, player.getName(), StringMgmt.join(args, " "));
		Bukkit.getOnlinePlayers().stream()
			.filter(p -> CultureUtil.isSameCulture(p, townCulture))
			.forEach(p -> Messaging.sendMessage(p, message));
	}

	private String getTownCultureOrThrow(Player player) throws TownyException {
		Town town = getTownFromResidentOrThrow(getResidentOrThrow(player));
		if (!TownMetaDataController.hasTownCulture(town))
			throw new TownyException(Translatable.of("msg_err_command_disable"));

		return TownyCultures.getCulture(town);
	}
}