package com.gmail.goosius.townycultures;

import com.gmail.goosius.townycultures.command.*;
import com.gmail.goosius.townycultures.listeners.TownyDynmapListener;
import com.gmail.goosius.townycultures.listeners.TownyListener;
import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.TownyCulturesSettings;
import com.gmail.goosius.townycultures.utils.CultureUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.gmail.goosius.townycultures.settings.Settings;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.Version;
import com.palmergames.util.StringMgmt;
import com.gmail.goosius.townycultures.listeners.NationEventListener;
import com.gmail.goosius.townycultures.listeners.TownEventListener;

public class TownyCultures extends JavaPlugin {
	
	private static TownyCultures plugin;
	public static String prefix = "[TownyCultures] ";
	private static Version requiredTownyVersion = Version.fromString("0.98.4.0");
	private static boolean dynmapTowny = false;

	public static TownyCultures getTownyCultures() {
		return plugin;
	}

    @Override
    public void onEnable() {
    	
    	plugin = this;
    	
    	printSickASCIIArt();
    	
        if (!townyVersionCheck(getTownyVersion())) {
            severe("Towny version does not meet required minimum version: " + requiredTownyVersion.toString());
            severe("Shutting down....");
            onDisable();
            return;
        } else {
            info("Towny version " + getTownyVersion() + " found.");
        }
        
        if (!Settings.loadSettingsAndLang()) {
        	severe("Shutting down....");
        	onDisable();
        }

		if(TownyCulturesSettings.isTownyCulturesEnabled()) {

			checkPlugins();

			registerListeners();

			registerCommands();

			info("TownyCultures loaded successfully.");
		} else {
			info("TownyCultures loaded successfully but is disabled by config.");
		}
    }
    
    private void checkPlugins() {
		Plugin test = getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (test != null) {
            new TownyCulturesPlaceholderExpansion(this).register();
            info("Found PlaceholderAPI. Enabling support...");
		}
		
		test = getServer().getPluginManager().getPlugin("Dynmap-Towny");
		if (test != null)
			dynmapTowny = true;
		
	}

	@Override
    public void onDisable() {
    	severe("Shutting down....");
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
		pm.registerEvents(new TownyListener(), this);
		if (dynmapTowny)
			pm.registerEvents(new TownyDynmapListener(), this);
	}
	
	private void registerCommands() {
		getCommand("culture").setExecutor(new CultureCommand());
		getCommand("cultureadmin").setExecutor(new CultureAdminCommand());
		getCommand("cc").setExecutor(new CultureChatCommand());
	}

	private void printSickASCIIArt() {
		Bukkit.getLogger().info("   .---.                                     ");
		Bukkit.getLogger().info("     |                                       ");
		Bukkit.getLogger().info("     | .-..  .    ._.--. .  .                ");
		Bukkit.getLogger().info("     |(   )\\  \\  /  |  | |  |              ");
		Bukkit.getLogger().info("     ' `-'  `' `'   '  `-`--|                ");
		Bukkit.getLogger().info("                            ;                ");
		Bukkit.getLogger().info("          .--.     . .   `-'                 ");
		Bukkit.getLogger().info("         :         |_|_                      ");
		Bukkit.getLogger().info("         |    .  . | |  .  . .--..-. .--.    ");
		Bukkit.getLogger().info("         :    |  | | |  |  | |  (.-' `--.    ");
		Bukkit.getLogger().info("          `--'`--`-`-`-'`--`-'   `--'`--'    ");
		Bukkit.getLogger().info("                          by Goosius & LlmDl ");
		Bukkit.getLogger().info("");
	}
	
	public static String getCulture(Player player) {
		Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
		if (resident == null)
			return Translatable.of("status_no_town").defaultLocale();
		return getCulture(resident);
	}
	
	public static String getCulture(Resident resident) {
		if (resident.hasTown())
			return getCulture(resident.getTownOrNull());
		return Translatable.of("status_no_town").defaultLocale();
	}
	
	public static String getCulture(Town town) {
		return StringMgmt.capitalize(TownMetaDataController.getTownCulture(town));
	}
	
	public static boolean hasCulture(Object obj) {
		if (obj instanceof Player) {
			return !CultureUtil.isValidCultureName(getCulture((Player) obj));
		} else if (obj instanceof Resident) {
			return !CultureUtil.isValidCultureName(getCulture((Resident) obj));
		} else if (obj instanceof Town) {
			return TownMetaDataController.hasTownCulture((Town) obj);
		}
		return false;
	}
	
	public static void info(String message) {
		plugin.getLogger().info(message);
	}
	
	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}
}
