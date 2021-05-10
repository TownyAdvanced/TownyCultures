package com.gmail.goosius.townycultures.utils;

import org.jetbrains.annotations.Nullable;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.util.NameValidation;

public class CultureUtil {

	/**
	 * Checks that a culture is named correctly, sets the name to lowercase.
	 * @param newCulture
	 * @return returns new name or null if name is invalid.
	 */
	@Nullable
	public static String validateCultureName(String newCulture) {
		String culture = "";
		if (!newCulture.equalsIgnoreCase("none")) {
			if (!NameValidation.isValidString(newCulture) || newCulture.length() > TownySettings.getMaxNameLength()) {
				return null;
			}
			culture = newCulture.toLowerCase();
		}
		return culture;
	}

	/**
	 * Given a resident, does this resident have the given culture?
	 * @param res Resident to check.
	 * @param culture Culture to check for.
	 * @return True if the resident has a town with the given culture.
	 */
	public static boolean isSameCulture(Resident res, String culture) {
		return res.hasTown() 
				&& TownMetaDataController.hasTownCulture(TownyAPI.getInstance().getResidentTownOrNull(res)) 
				&& TownMetaDataController.getTownCulture(TownyAPI.getInstance().getResidentTownOrNull(res)).equalsIgnoreCase(culture); 
	}
}
