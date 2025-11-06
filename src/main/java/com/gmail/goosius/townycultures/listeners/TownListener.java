package com.gmail.goosius.townycultures.listeners;

import com.gmail.goosius.townycultures.settings.Settings;
import com.gmail.goosius.townycultures.utils.PresetCulturesUtil;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownListener implements Listener {

    /**
     * If cultural regions is enabled, automatically set the culture of a new town
     * 
     * @param newTownEvent the new town event
     */
	@EventHandler
	public void onCreateNewTown(NewTownEvent newTownEvent) {
        if(!Settings.isTownyCulturesEnabled())
            return;
        if(!Settings.isPresetCulturesEnabled()) 
            return;
        PresetCulturesUtil.pickCultureAutomatically(newTownEvent.getTown());
	}
}
