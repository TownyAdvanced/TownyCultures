package com.gmail.goosius.townycultures;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class TownyCulturesPlaceholderExpansion extends PlaceholderExpansion {

	private final TownyCultures plugin;
	
    public TownyCulturesPlaceholderExpansion(TownyCultures plugin) {
    	this.plugin = plugin;
    }

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getIdentifier() {
		return "townycultures";
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {

		if (player == null) {
			return "";
		}
	
		switch (identifier) {
		case "culture": // %townycultures_culture&
			return TownyCultures.getCulture(player);

		default:
			return null;
		}
	}
}
