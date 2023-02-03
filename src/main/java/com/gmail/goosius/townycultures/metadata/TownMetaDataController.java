package com.gmail.goosius.townycultures.metadata;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

/**
 * 
 * @author Goosius
 *
 */
public class TownMetaDataController {

	private static StringDataField townCulture = new StringDataField("townycultures_culture", "");

	public static String getTownCulture(Town town) {
		if (MetaDataUtil.hasMeta(town, townCulture))
			return MetaDataUtil.getString(town, townCulture);
		else
			return Translatable.of("status_unknown").defaultLocale();
	}

	public static void setTownCulture(Town town, String culture) {
		// Remove old meta and replace with nothing.
		if (MetaDataUtil.hasMeta(town, townCulture) && culture.isEmpty())
			town.removeMetaData("townycultures_culture", true);
		// Nothing left to do, we either just removed the culture or they had none to begin with.
		if (culture.isEmpty())
			return;
		// Set the new meta.
		MetaDataUtil.addNewStringMeta(town, "townycultures_culture", culture, true);
	}

	public static boolean hasTownCulture(Town town) {
		return MetaDataUtil.hasMeta(town, townCulture);
	}


}
