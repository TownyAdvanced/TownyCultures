package com.gmail.goosius.siegewar.command;

import com.gmail.goosius.siegewar.enums.SiegeWarPermissionNodes;
import com.gmail.goosius.siegewar.metadata.TownMetaDataController;
import com.gmail.goosius.siegewar.settings.Translation;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.ChatTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class CultureSetCommand implements CommandExecutor, TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return new ArrayList<>();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player) || args == null)
			return true;

		Player player = (Player)sender;
		Resident resident = TownyUniverse.getInstance().getResident(player.getName());
		if (resident == null)
			return true;

		if (args.length != 1)
			sender.sendMessage(ChatTools.formatCommand("Eg", "/setculture", "[culture]", ""));

		/*
		 * Check for resident + mayor
		 * Note that I didn't add a permission for this
		 * because it would have required an new manual townyperms setup step,
		 * thus providing little benefit at the cost of introducing a manual fail point
		 */
		if(!resident.hasTown() || !resident.isMayor())
			player.sendMessage(Translation.of("msg_err_command_disable"));

		//Set town culture
		try {
			Town town = resident.getTown();
			TownMetaDataController.setTownCulture(town, args[0]);
			//Todo - success message
		} catch (NotRegisteredException e) {
			//Won't get here as we already checked for resident
		}

		return true;
	}
}