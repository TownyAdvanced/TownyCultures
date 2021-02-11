package com.gmail.goosius.townycultures.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.gmail.goosius.townycultures.settings.Translation;
import com.palmergames.bukkit.towny.event.statusscreen.NationStatusScreenEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.util.StringMgmt;

public class NationEventListener implements Listener {

	/*
	 * Adds a cultural breakdown of the Towns in a nation,
	 * using town population to determine culture-strength.
	 */
	@EventHandler
	public void onNationStatus(NationStatusScreenEvent event) {
		if (TownyCulturesSettings.isTownyCulturesEnabled()) {
			
			/*
			 * Create a map of cultures within the nation, from which the town
			 * populations are used to calculate the strength of the culture.
			 */
			Map<String,Integer> cultureStrength = new HashMap<>();
			for (Town town : event.getNation().getTowns())
				cultureStrength = assignStrength(town, town.getNumResidents(), cultureStrength);
			
			/*
			 * Doubles cast to int will round downwards resulting in a likely chance 
			 * that there will be some remaining percents left over after strengths
			 * are calculated. If there is any remainder this will either:
			 * - assign it to the capital city if it has a culture, or
			 * - prescribe it as an unknown.
			 */
			double pop = event.getNation().getNumResidents();
			int remainder = findRemainder(pop, cultureStrength);
			if (remainder > 0)
				cultureStrength = assignStrength(event.getNation().getCapital(), remainder, cultureStrength);
			
			// Sort the map from strongest culture to weakest culture.
			Map<String, Integer> sortedMap = sortMap(cultureStrength);
			
			// Turn it into a list of "Culturename ###%" strings.
			List<String> cultures = new ArrayList<>(sortedMap.size());
			for (String culture : sortedMap.keySet()) {
				int percent = (int) (sortedMap.get(culture) / pop * 100);
				cultures.add(StringMgmt.capitalize(culture) + " " + percent + "%");
			}

			// Join the above lines if needed.
			String output = "";
			if (cultures.size() > 1)
				output = String.join(", ", cultures);
			else 
				output = cultures.get(0);
			
			// Add our line to the NationStatusScreenEvent.
			event.addLines(Arrays.asList(Translation.of("status_town_culture", output)));
		}
	}

	/*
	 * Assigns the strength to the Town's culture or to the "Unknown" category.
	 */
	private Map<String, Integer> assignStrength(Town town, int strength, Map<String, Integer> cultureStrength) {
		String culture;
		if (TownMetaDataController.hasTownCulture(town))
			culture = TownMetaDataController.getTownCulture(town);
		else 
			culture = "Unknown";

		if (cultureStrength.containsKey(culture))
			strength += cultureStrength.get(culture);
		
		cultureStrength.put(culture, strength);
		return cultureStrength;
	}

	private int findRemainder(double pop, Map<String, Integer> cultureStrength) {
		int remainder = 100;
		for (String culture : cultureStrength.keySet()) {
			int percent = (int) (cultureStrength.get(culture) / pop * 100);
			remainder -= percent;
		}
		return remainder;
	}

	private Map<String, Integer> sortMap(Map<String, Integer> cultureStrength) {
		
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(cultureStrength.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
	}
}
