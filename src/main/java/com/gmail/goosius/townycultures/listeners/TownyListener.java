package com.gmail.goosius.townycultures.listeners;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.goosius.townycultures.TownyCultures;
import com.palmergames.bukkit.towny.event.TranslationLoadEvent;
import com.palmergames.bukkit.towny.object.TranslationLoader;

public class TownyListener implements Listener {

	@EventHandler
	public void onLoadTranslations(TranslationLoadEvent event) {
		
		Plugin plugin = TownyCultures.getTownyCultures(); 
		Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
		TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownyCultures.class);
		loader.load();
		Map<String, Map<String, String>>  translations = loader.getTranslations();
		
		for (String language : translations.keySet())
			for (Map.Entry<String, String> map : translations.get(language).entrySet())
				event.addTranslation(language, map.getKey(), map.getValue());

        TownyAPI.getInstance().addTranslations(plugin, loader.getTranslations());
    }
}
