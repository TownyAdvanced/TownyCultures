package com.gmail.goosius.townycultures.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.gmail.goosius.townycultures.settings.Translation;
import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class NationEventListener implements Listener {

	@EventHandler
	public void onNationStatus(NationStatusScreenEvent event) {
		if (TownyCulturesSettings.isTownyCulturesEnabled()) {
			Nation nation = event.getNation();
			Map<String,Integer> cultureStrength = new HashMap<>();
			
			for (Town town : nation.getTowns()) {
				if (TownMetaDataController.hasTownCulture(town)) {
					int strength = town.getNumResidents();
					String culture = TownMetaDataController.getTownCulture(town);
					if (cultureStrength.containsKey(culture))
						strength += cultureStrength.get(culture);
					
					cultureStrength.put(culture, strength);
				}
			}
			
			if (cultureStrength.size() == 0)
				return;
			
			List<String> cultures = new ArrayList<>(cultureStrength.size());
			int pop = nation.getNumResidents();
			for (String culture : cultureStrength.keySet()) {
				int percent = (int) (cultureStrength.get(culture) / pop * 100);
				cultures.add(culture + " " + percent + "%");
			}

			String output = "";
			if (cultures.size() > 1)
				output = String.join(", ", cultures);
			else 
				output = cultures.get(0);
			
			event.setLines(Arrays.asList(Translation.of("status_town_culture", output)));
		}
	}

}
