package com.gmail.goosius.siegewar.command;

import com.gmail.goosius.siegewar.enums.SiegeWarPermissionNodes;
import com.gmail.goosius.siegewar.metadata.SiegeMetaDataController;
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
		List<String> allwords = new ArrayList<>();
		allwords.add("town");
		allwords.add("set");
		allwords.add("culture");
		return allwords;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player && args.length == 1) {
			Player player = (Player)sender;

			//Check for permission
			if (!player.hasPermission(SiegeWarPermissionNodes.SIEGEWAR_COMMAND_SIEGEWAR_NATION.getNode(args[0]))) {
				player.sendMessage(Translation.of("msg_err_command_disable"));
				return true;
			}

			//Check for resident
			Resident resident = TownyUniverse.getInstance().getResident(player.getName());
			if (resident == null)
				return true;

			//Set culture
			try {
				Town town = resident.getTown();
				SiegeMetaDataController.setTownCulture(town, args[0]);
			} catch (NotRegisteredException e) {
				//No town //Todo
			}

		} else {
			sender.sendMessage(ChatTools.formatCommand("Eg", "/town set culture", "[culture]", ""));
		}
		return true;
	}
}