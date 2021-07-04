package com.gmail.goosius.townycultures.settings;

public class TownyCulturesSettings {

	public static boolean isTownyCulturesEnabled() {
		return Settings.getBoolean(ConfigNodes.TOWNY_CULTURES_ENABLED);
	}
	
	public static int maxNameLength() {
		return Settings.getInt(ConfigNodes.MAXIMUM_NAME_LENGTH);
	}
}
