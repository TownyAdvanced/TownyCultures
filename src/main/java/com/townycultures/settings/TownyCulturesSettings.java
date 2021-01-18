package com.townycultures.settings;

public class TownyCulturesSettings {

	public static boolean isTownyCulturesEnabled() {
		return Settings.getBoolean(ConfigNodes.TOWNY_CULTURES_ENABLED);
	}
}
