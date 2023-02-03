package com.gmail.goosius.townycultures.utils;

import org.bukkit.entity.Player;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.gmail.goosius.townycultures.settings.Settings;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.NameValidation;

public class CultureUtil {

	/**
	 * Checks that a culture is named correctly, sets the name to lowercase.
	 * @param newCulture
	 * @return returns new name or null if name is invalid.
	 */
	public static String validateCultureName(String newCulture) throws TownyException {
		String culture = "";
		if (!newCulture.equalsIgnoreCase("none")) {
			if (!NameValidation.isValidString(newCulture))
				throw new TownyException(Translatable.of("msg_err_invalid_characters"));

			if (newCulture.length() > Settings.maxNameLength()) 
				throw new TownyException(Translatable.of("msg_err_culture_name_too_long", Settings.maxNameLength()));

			culture = newCulture.toLowerCase();
		}
		return culture;
	}

	public static boolean isValidCultureName(String culture) {
		return !culture.isEmpty() && !culture.equalsIgnoreCase("none") && !culture.equalsIgnoreCase("unknown");
	}
	
	/**
	 * Given a resident, does this resident have the given culture?
	 * @param res Resident to check.
	 * @param culture Culture to check for.
	 * @return True if the resident has a town with the given culture.
	 */
	public static boolean isSameCulture(Resident res, String culture) {
		return res.hasTown() && TownMetaDataController.hasTownCulture(res.getTownOrNull())
				&& TownMetaDataController.getTownCulture(res.getTownOrNull()).equalsIgnoreCase(culture);
	}

	/**
	 * Given a player, does this playerhave the given culture?
	 * @param player Player to check.
	 * @param culture Culture to check for.
	 * @return True if the player has the culture.
	 */
	public static boolean isSameCulture(Player player, String culture) {
		Resident res = TownyAPI.getInstance().getResident(player);
		return res != null && isSameCulture(res, culture);
	}
}
