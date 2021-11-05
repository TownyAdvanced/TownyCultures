package com.gmail.goosius.townycultures.command;

import com.gmail.goosius.townycultures.Messaging;
import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.util.BukkitTools;
import com.palmergames.bukkit.util.ChatTools;
import com.palmergames.util.StringMgmt;
import com.gmail.goosius.townycultures.settings.Translation;
import com.gmail.goosius.townycultures.utils.CultureUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class CultureChatCommand implements CommandExecutor, TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

	private void showCultureCommunicationHelp(CommandSender sender) {
		sender.sendMessage(ChatTools.formatCommand("Eg", "/cc", "[msg]", ""));
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
		//Ensure the sender is in a town
		String townCulture = "";
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		if (resident != null && resident.hasTown() && TownMetaDataController.hasTownCulture(TownyAPI.getInstance().getResidentTownOrNull(resident))) {
			townCulture = TownyCultures.getCulture(TownyAPI.getInstance().getResidentTownOrNull(resident));
		} else {
			Messaging.sendErrorMsg(player, Translation.of("msg_err_command_disable"));
			return;
		}

		String formattedMessage = Translation.of("culture_chat_message", townCulture, resident.getName(), StringMgmt.join(args, " "));

		Resident otherResident;
		for(Player otherPlayer: BukkitTools.getOnlinePlayers()) {
			otherResident = TownyUniverse.getInstance().getResident(otherPlayer.getUniqueId());
			if (otherResident != null && CultureUtil.isSameCulture(otherResident, townCulture)) {

				//Send message
				otherPlayer.sendMessage(formattedMessage);
				//TownyMessaging........
			}
		}
	}
}