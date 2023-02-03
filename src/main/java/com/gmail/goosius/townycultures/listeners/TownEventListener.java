package com.gmail.goosius.townycultures.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.goosius.townycultures.TownyCultures;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.palmergames.adventure.text.Component;
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
		if (!TownyCulturesSettings.isTownyCulturesEnabled())
			return;
		String slug = TownMetaDataController.hasTownCulture(event.getTown()) ? TownyCultures.getCulture(event.getTown()) : "/town set culture [culture]";
		event.getStatusScreen().addComponentOf("culture", Component.text(Translatable.of("status_town_culture", slug).forLocale(event.getCommandSender())));
	}
}
