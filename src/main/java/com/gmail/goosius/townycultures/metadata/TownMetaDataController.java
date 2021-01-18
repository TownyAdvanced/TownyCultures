package com.gmail.goosius.townycultures.metadata;

import com.gmail.goosius.townycultures.TownyCultures;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;

/**
 * 
 * @author Goosius
 *
 */
public class TownMetaDataController {

	@SuppressWarnings("unused")
	private TownyCultures plugin;
	private static StringDataField townCulture = new StringDataField("townycultures_culture", "");

	public TownMetaDataController(TownyCultures plugin) {
		this.plugin = plugin;
	}

	public static String getTownCulture(Town town) {
		StringDataField sdf = (StringDataField) townCulture.clone();
		if (town.hasMeta(sdf.getKey()))
			return MetaDataUtil.getString(town, sdf);
		else
			return "/culture set [culture]";
	}

	public static void setTownCulture(Town town, String culture) {
		StringDataField sdf = (StringDataField) townCulture.clone();
		if (town.hasMeta(sdf.getKey()))
			MetaDataUtil.setString(town, sdf, culture);
		else
			town.addMetaData(new StringDataField("townycultures_culture", culture));
	}


}
