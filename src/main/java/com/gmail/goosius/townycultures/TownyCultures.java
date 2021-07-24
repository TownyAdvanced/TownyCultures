package com.gmail.goosius.townycultures;

import com.gmail.goosius.townycultures.command.*;
import com.gmail.goosius.townycultures.listeners.TownyDynmapListener;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.gmail.goosius.townycultures.settings.Settings;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.util.Version;
import com.gmail.goosius.townycultures.listeners.NationEventListener;
import com.gmail.goosius.townycultures.listeners.TownEventListener;

public class TownyCultures extends JavaPlugin {
	
	private static TownyCultures plugin;
	public static String prefix = "[TownyCultures] ";
	private static Version requiredTownyVersion = Version.fromString("0.97.0.16");
	private static boolean dynmapTowny = false;

	public static TownyCultures getTownyCultures() {
		return plugin;
	}

    @Override
    public void onEnable() {
    	
    	plugin = this;
    	
    	printSickASCIIArt();
    	
        if (!townyVersionCheck(getTownyVersion())) {
            System.err.println(prefix + "Towny version does not meet required minimum version: " + requiredTownyVersion.toString());
            System.err.println(prefix + "Shutting down....");
            onDisable();
            return;
        } else {
            System.out.println(prefix + "Towny version " + getTownyVersion() + " found.");
        }
        
        if (!Settings.loadSettingsAndLang()) {
        	System.err.println(TownyCultures.prefix + "Shutting down....");
        	onDisable();
        }

		if(TownyCulturesSettings.isTownyCulturesEnabled()) {

			checkPlugins();

			registerListeners();

			registerCommands();

			System.out.println(prefix + "TownyCultures loaded successfully.");
		} else {
			System.out.println(prefix + "TownyCultures loaded successfully but is disabled by config.");
		}
    }
    
    private void checkPlugins() {
		Plugin test = getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (test != null) {
            new TownyCulturesPlaceholderExpansion(this).register();
            System.out.println(prefix + "Found PlaceholderAPI. Enabling support...");
		}
		
		test = getServer().getPluginManager().getPlugin("Dynmap-Towny");
		if (test != null)
			dynmapTowny = true;
		
	}

	@Override
    public void onDisable() {
    	System.err.println(prefix + "Shutting down....");
    }

	public String getVersion() {
		return this.getDescription().getVersion();
	}
	
    private boolean townyVersionCheck(String version) {
        return Version.fromString(version).compareTo(requiredTownyVersion) >= 0;
    }

    private String getTownyVersion() {
        return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
    }
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new TownEventListener(), this);
		pm.registerEvents(new NationEventListener(), this);
		if (dynmapTowny)
			pm.registerEvents(new TownyDynmapListener(), this);
	}
	
	private void registerCommands() {
		getCommand("culture").setExecutor(new CultureCommand());
		getCommand("cultureadmin").setExecutor(new CultureAdminCommand());
		getCommand("cc").setExecutor(new CultureChatCommand());
	}

	private void printSickASCIIArt() {
		System.out.println("   .---.                                     ");
		System.out.println("     |                                       ");
		System.out.println("     | .-..  .    ._.--. .  .                ");
		System.out.println("     |(   )\\  \\  /  |  | |  |              ");
		System.out.println("     ' `-'  `' `'   '  `-`--|                ");
		System.out.println("                            ;                ");
		System.out.println("          .--.     . .   `-'                 ");
		System.out.println("         :         |_|_                      ");
		System.out.println("         |    .  . | |  .  . .--..-. .--.    ");
		System.out.println("         :    |  | | |  |  | |  (.-' `--.    ");
		System.out.println("          `--'`--`-`-`-'`--`-'   `--'`--'    ");
		System.out.println("                          by Goosius & LlmDl ");
		System.out.println("");
	}
	
	public static String getCulture(Player player) {
		Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
		if (resident == null)
			return "None";
		return getCulture(resident);
	}
	
	public static String getCulture(Resident resident) {
		if (resident.hasTown())
			return getCulture(resident.getTownOrNull());
		return "None";
	}
	
	public static String getCulture(Town town) {
		return TownMetaDataController.getTownCulture(town);
	}
}
