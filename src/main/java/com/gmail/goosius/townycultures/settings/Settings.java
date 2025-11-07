package com.gmail.goosius.townycultures.settings;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import com.gmail.goosius.townycultures.objects.PresetCulture;
import com.gmail.goosius.townycultures.enums.AutomaticCultureSelectionType;
import com.palmergames.bukkit.towny.object.Coord;
import org.bukkit.plugin.Plugin;

import com.gmail.goosius.townycultures.TownyCultures;
import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.util.FileMgmt;

public class Settings {
	private static CommentedConfiguration config, newConfig;
	private static Path configPath = TownyCultures.getTownyCultures().getDataFolder().toPath().resolve("config.yml");
    private final static Pattern LIST_REGEX_PATTERN = Pattern.compile("\\{([^}]+)}", Pattern.CASE_INSENSITIVE);

	public static boolean loadSettingsAndLang() {
		try {
			loadConfig();
		} catch (IOException e) {
			TownyCultures.severe(String.format("Loading error: Failed to load file %s (does it pass a yaml parser?).", configPath));
			TownyCultures.severe("https://jsonformatter.org/yaml-parser");
			TownyCultures.severe(e.getMessage());
			TownyCultures.severe("Config.yml failed to load! Disabling!");
			return false;
		}

		try {
			Plugin plugin = TownyCultures.getTownyCultures(); 
			Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
			TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownyCultures.class);
			loader.load();
			TownyAPI.getInstance().addTranslations(plugin, loader.getTranslations());
		} catch (TownyInitException e) {
			e.printStackTrace();
			TownyCultures.severe("Locale files failed to load! Disabling!");
			return false;
		}

		return true;
	}
	
	private static void loadConfig() throws IOException {
		if (FileMgmt.checkOrCreateFile(configPath.toString())) {
			// read the config.yml into memory
			config = new CommentedConfiguration(configPath);
			if (!config.load())
				TownyCultures.severe("Failed to load Config!");

			setDefaults(TownyCultures.getTownyCultures().getVersion(), configPath);
			config.save();
		}
	}

	public static void addComment(String root, String... comments) {

		newConfig.addComment(root.toLowerCase(), comments);
	}

	private static void setNewProperty(String root, Object value) {

		if (value == null) {
			value = "";
		}
		newConfig.set(root.toLowerCase(), value.toString());
	}

	private static void setProperty(String root, Object value) {

		config.set(root.toLowerCase(), value.toString());
	}

	public static String getLastRunVersion(String currentVersion) {

		return getString(ConfigNodes.LAST_RUN_VERSION.getRoot(), currentVersion);
	}

	/**
	 * Builds a new config reading old config data.
	 */
	private static void setDefaults(String version, Path configPath) {

		newConfig = new CommentedConfiguration(configPath);
		newConfig.load();

		for (ConfigNodes root : ConfigNodes.values()) {
			if (root.getComments().length > 0)
				addComment(root.getRoot(), root.getComments());
			if (root.getRoot() == ConfigNodes.VERSION.getRoot())
				setNewProperty(root.getRoot(), version);
			else if (root.getRoot() == ConfigNodes.LAST_RUN_VERSION.getRoot())
				setNewProperty(root.getRoot(), getLastRunVersion(version));
			else
				setNewProperty(root.getRoot(), (config.get(root.getRoot().toLowerCase()) != null) ? config.get(root.getRoot().toLowerCase()) : root.getDefault());
		}

		config = newConfig;
		newConfig = null;
	}

	public static String getString(String root, String def) {

		String data = config.getString(root.toLowerCase(), def);
		if (data == null) {
			sendError(root.toLowerCase() + " from config.yml");
			return "";
		}
		return data;
	}

	private static void sendError(String msg) {

		TownyCultures.severe("Error could not read " + msg);
	}

	public static boolean getBoolean(ConfigNodes node) {

		return Boolean.parseBoolean(config.getString(node.getRoot().toLowerCase(), node.getDefault()));
	}

	public static double getDouble(ConfigNodes node) {

		try {
			return Double.parseDouble(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0.0;
		}
	}

	public static int getInt(ConfigNodes node) {

		try {
			return Integer.parseInt(config.getString(node.getRoot().toLowerCase(), node.getDefault()).trim());
		} catch (NumberFormatException e) {
			sendError(node.getRoot().toLowerCase() + " from config.yml");
			return 0;
		}
	}

	public static String getString(ConfigNodes node) {

		return config.getString(node.getRoot().toLowerCase(), node.getDefault());
	}

	public static void setLastRunVersion(String currentVersion) {

		setProperty(ConfigNodes.LAST_RUN_VERSION.getRoot(), currentVersion);
		config.save();
	}

	public static boolean isTownyCulturesEnabled() {
		return Settings.getBoolean(ConfigNodes.TOWNY_CULTURES_ENABLED);
	}

	public static int maxNameLength() {
		return Settings.getInt(ConfigNodes.MAXIMUM_NAME_LENGTH);
	}

    public static boolean isPresetCulturesEnabled() {
        return getBoolean(ConfigNodes.PRESET_CULTURES_ENABLED);
    }

    public static AutomaticCultureSelectionType getAutomaticCultureSelectionType() {
        return AutomaticCultureSelectionType.parseText(getString(ConfigNodes.PRESET_CULTURES_AUTOMATIC_CULTURE_SELECTION_TYPE));
    }

    public static List<PresetCulture> getPresetCulturesList() {
        List<PresetCulture> presetCulturesAsList = new ArrayList<>();
        String presetCulturesListAsString = getString(ConfigNodes.PRESET_CULTURES_LIST);

        if(!presetCulturesListAsString.isEmpty()) {
            Matcher matcher = LIST_REGEX_PATTERN.matcher(presetCulturesListAsString);
            String presetCultureAsString;
            String[] presetCultureAsArray;
            PresetCulture presetCulture;

            while (matcher.find()) {
                //Read one culture
                presetCultureAsString = matcher.group(1);
                presetCultureAsArray = presetCultureAsString.replaceAll("\\[", "").replaceAll("]", "").split(",");
                presetCulture = getPresetCulture(presetCultureAsArray);
                presetCulturesAsList.add(presetCulture);
            }
        }
        return presetCulturesAsList;
    }

    private static @NotNull PresetCulture getPresetCulture(String[] singleCultureAsArray) {
        Coord topLeftCoord = Coord.parseCoord(Integer.parseInt(singleCultureAsArray[0].trim()), Integer.parseInt(singleCultureAsArray[1].trim()));
        Coord bottomRightCoord = Coord.parseCoord(Integer.parseInt(singleCultureAsArray[2].trim()), Integer.parseInt(singleCultureAsArray[3].trim()));
        String cultureName = singleCultureAsArray[4].trim();
        String cultureDescription = singleCultureAsArray[5].trim();
        return new PresetCulture(topLeftCoord,bottomRightCoord,cultureName,cultureDescription);
    }
}
