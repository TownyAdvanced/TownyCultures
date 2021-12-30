package com.gmail.goosius.townycultures.settings;

import com.gmail.goosius.townycultures.TownyCultures;
import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.bukkit.towny.command.HelpMenu;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.util.StringMgmt;

import com.gmail.goosius.townycultures.utils.FileMgmt;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * A convenience object to facilitate translation. 
 */
public final class Translation {
	
	public static CommentedConfiguration language;

	// This will read the language entry in the config.yml to attempt to load
	// custom languages
	// if the file is not found it will load the default from resource
	public static void loadLanguage(String filepath, String defaultRes) throws IOException {

		String res = Settings.getString(ConfigNodes.LANGUAGE.getRoot(), defaultRes);
		String fullPath = filepath + File.separator + res;
		File file = FileMgmt.unpackResourceFile(fullPath, res, defaultRes);

		// read the (language).yml into memory
		language = new CommentedConfiguration(file.toPath());
		language.load();
		HelpMenu.loadMenus();
		CommentedConfiguration newLanguage = new CommentedConfiguration(file.toPath());
		
		try {
			newLanguage.loadFromString(FileMgmt.convertStreamToString("/" + res));
		} catch (IOException e) {
			TownyCultures.info("Lang: Custom language file detected, not updating.");
			TownyCultures.info("Lang: " + res + " v" + Translation.of("version") + " loaded.");
			return;
		} catch (InvalidConfigurationException e) {
			TownyCultures.severe("Invalid Configuration in language file detected.");
		}
		
		String resVersion = newLanguage.getString("version");
		String langVersion = Translation.of("version");

		if (!langVersion.equalsIgnoreCase(resVersion)) {
			language = newLanguage;
			TownyCultures.info("Lang: Language file replaced with updated version.");
			FileMgmt.stringToFile(FileMgmt.convertStreamToString("/" + res), file);
		}
		TownyCultures.info("Lang: " + res + " v" + Translation.of("version") + " loaded.");
	}

	private static String parseSingleLineString(String str) {
		return Colors.translateColorCodes(str);
	}
	
	/**
	 * Translates give key into its respective language. 
	 * 
	 * @param key The language key.
	 * @return The localized string.
	 */
	public static String of(String key) {
		String data = language.getString(key.toLowerCase());

		if (data == null) {
			TownyCultures.severe("Error could not read " + key.toLowerCase() + " from " + Settings.getString(ConfigNodes.LANGUAGE));
			return "";
		}
		return StringMgmt.translateHexColors(parseSingleLineString(data));
	}

	/**
	 * Translates give key into its respective language. 
	 *
	 * @param key The language key.
	 * @param args The arguments to format the localized string.   
	 * @return The localized string.
	 */
	public static String of(String key, Object... args) {
		return String.format(of(key), args);
	}

	private Translation() {}
}