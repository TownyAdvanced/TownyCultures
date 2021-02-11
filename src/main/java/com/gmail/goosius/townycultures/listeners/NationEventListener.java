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

			//Get total nation population
			double totalNationPopulation = event.getNation().getNumResidents();

			/*
			 * Create a map of cultures within the nation, from which the town
			 * populations are used to calculate the strength of the culture.
			 */
			Map<String,Integer> cultureStrength = new HashMap<>();
			for (Town town : event.getNation().getTowns())
				assignStrength(town, cultureStrength);

			// Sort the map from strongest culture to weakest culture.
			Map<String, Integer> sortedCultureStrengthMap = sortMap(cultureStrength);

			/*
			 * Create a double map of culture percentages
			 */
			Map<String,Double> culturePercentageDouble = new HashMap<>();
			for (Map.Entry<String, Integer> entry: sortedCultureStrengthMap.entrySet()) {
				double percentDouble = ((double)entry.getValue()) / totalNationPopulation * 100;
				culturePercentageDouble.put(entry.getKey(), percentDouble);
			}

			/*
			 * Create a double map of the main cultures only
			 * i.e. for cultures of less than 1%, combine them as "Other"
			 */
			Map<String,Double> mainCulturePercentageDouble = new HashMap<>();
			for (Map.Entry<String, Double> entry: culturePercentageDouble.entrySet()) {
				if(entry.getValue() < 1) {
					if(mainCulturePercentageDouble.containsKey("Other")) {
						double newPercentage = mainCulturePercentageDouble.get("Other") + entry.getValue();
						mainCulturePercentageDouble.put("Other", newPercentage);
					} else {
						mainCulturePercentageDouble.put("Other", entry.getValue());
					}
				} else {
					mainCulturePercentageDouble.put(entry.getKey(), entry.getValue());
				}
			}

			/*
			 * Create an int map of culture percentages
			 */
			Map<String,Integer> mainCulturePercentageInteger = new HashMap<>();
			for (Map.Entry<String, Double> entry: mainCulturePercentageDouble.entrySet()) {
				mainCulturePercentageInteger.put(entry.getKey(), (int) (entry.getValue() + 0.5)); //Round half up
			}

			/*
			 * Check if the percentages add up to 100% at this point. If not:
			 * - assign the remainder to the capital city if it has a culture, or
			 * - prescribe it as an unknown.
			 */
			int totalCulturePercent = 0;
			for(int individualCulturePercent: mainCulturePercentageInteger.values()) {
				totalCulturePercent += individualCulturePercent;
			}
			int remainder = totalCulturePercent - 100;
			if(remainder != 0) {
				Town capital = event.getNation().getCapital();
				String capitalCulture;
				int updatedPercent;
				if (TownMetaDataController.hasTownCulture(capital)) {
					capitalCulture = TownMetaDataController.getTownCulture(capital);
				} else {
					capitalCulture = "Unknown";
				}
				updatedPercent = mainCulturePercentageInteger.get(capitalCulture) + remainder;
				mainCulturePercentageInteger.put(capitalCulture, updatedPercent);
			}

			// Turn it into a list of "Culturename ###%" strings.
			List<String> cultures = new ArrayList<>(mainCulturePercentageInteger.size());
			for (Map.Entry<String, Integer> entry: mainCulturePercentageInteger.entrySet()) {
				cultures.add(StringMgmt.capitalize(entry.getKey()) + " " + entry.getValue() + "%");
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
	private void assignStrength(Town town, Map<String, Integer> cultureStrength) {
		String culture;
		if (TownMetaDataController.hasTownCulture(town))
			culture = TownMetaDataController.getTownCulture(town);
		else 
			culture = "Unknown";

		if (cultureStrength.containsKey(culture)) {
			//Culture already exists. Add the town population to it
			cultureStrength.put(culture, cultureStrength.get(culture) + town.getNumResidents());
		} else {
			//Culture does not already exist. Create it
			cultureStrength.put(culture, town.getNumResidents());
		}
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
