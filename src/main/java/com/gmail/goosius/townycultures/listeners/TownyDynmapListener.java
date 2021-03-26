package com.gmail.goosius.townycultures.listeners;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.towny.events.BuildTownMarkerDescriptionEvent;

public class TownyDynmapListener implements Listener {

    /**
     * This method updates the town popup box on Dynmap-Towny
     *
     * 1. It looks for the %culture% tag in the popup
     * 2. If the %culture% tag exists, it replaces it with the town culture (or blank if there is no town culture)
     */
    @EventHandler
    public void on(BuildTownMarkerDescriptionEvent event) {
            if (event.getDescription().contains("%culture%")) {
                String finalDescription;
                    if (TownMetaDataController.hasTownCulture(event.getTown())) {
                        finalDescription = event.getDescription().replace("%culture%", TownMetaDataController.getTownCulture(event.getTown()));
                    } else {
                        finalDescription = event.getDescription().replace("%culture%", "");
                    }
                event.setDescription(finalDescription);
        }
    }
}
