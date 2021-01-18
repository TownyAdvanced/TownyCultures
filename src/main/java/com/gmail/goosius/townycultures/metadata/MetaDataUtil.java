package com.gmail.goosius.townycultures.metadata;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.CustomDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;

/**
 * 
 * @author LlmDl
 *
 */
class MetaDataUtil {

	private static void saveTown(Town town) {
		TownyUniverse.getInstance().getDataSource().saveTown(town);
	}

	static String getString(Town town, StringDataField sdf) {
		CustomDataField<?> cdf = town.getMetadata(sdf.getKey());
		if (cdf instanceof StringDataField) {
			return ((StringDataField) cdf).getValue();
		}
		return "";
	}

	static void setString(Town town, StringDataField sdf, String string) {
		CustomDataField<?> cdf = town.getMetadata(sdf.getKey());
		if (cdf instanceof StringDataField) {
			StringDataField value = (StringDataField) cdf;
			value.setValue(string);
			saveTown(town);
		}
	}
}
