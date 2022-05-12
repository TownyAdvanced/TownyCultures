package com.gmail.goosius.townycultures.listeners;

import java.util.Arrays;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Translatable;

/**
 * 
 * @author Goosius
 *
 */
public class TownEventListener implements Listener {

	@EventHandler
	public void onTownStatusScreen(TownStatusScreenEvent event) {
		if (TownyCulturesSettings.isTownyCulturesEnabled())
			if (TownMetaDataController.hasTownCulture(event.getTown()))
				event.addLines(Arrays.asList(Translatable.of("status_town_culture", TownyCultures.getCulture(event.getTown())).forLocale(event.getCommandSender())));
			else
				event.addLines(Arrays.asList(Translatable.of("status_town_culture", "/culture set [culture]").forLocale(event.getCommandSender())));
	}
}
