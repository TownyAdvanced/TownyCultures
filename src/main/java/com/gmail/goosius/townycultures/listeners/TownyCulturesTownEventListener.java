package com.gmail.goosius.townycultures.listeners;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.gmail.goosius.townycultures.settings.Translation;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;

/**
 * 
 * @author Goosius
 *
 */
public class TownyCulturesTownEventListener implements Listener {

	@SuppressWarnings("unused")
	private final TownyCultures plugin;
	
	public TownyCulturesTownEventListener(TownyCultures instance) {
		plugin = instance;
	}

	@EventHandler
	public void onTownStatusScreen(TownStatusScreenEvent event) {
		if (TownyCulturesSettings.isTownyCulturesEnabled()) {
			List<String> out = new ArrayList<>();
			Town town = event.getTown();

			//Culture: Azurian
			if(TownyCulturesSettings.isTownyCulturesEnabled()) {
				out.add(Translation.of("status_town_culture", TownMetaDataController.getTownCulture(town)));
			}

	        event.addLines(out);
		}
	}
}
