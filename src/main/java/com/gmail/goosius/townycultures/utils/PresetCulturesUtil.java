package com.gmail.goosius.townycultures.utils;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.objects.PresetCulture;
import com.gmail.goosius.townycultures.settings.Settings;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.Town;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PresetCulturesUtil {

    /**
     * Convenience map of the preset cultures, to allow faster loading than config file reads
     */
    private static final Map<String, PresetCulture> presetCulturesMap = new HashMap<>();

    /**
     * Clear the preset cultures map
     */
    public static void clearPresetCultures() {
        presetCulturesMap.clear();
    }

    /**
     * Load the preset cultures from the config file into the convenience-map
     */
    public static void loadPresetCultures() {
        List<PresetCulture> presetCulturesList = Settings.getPresetCulturesList();
        for(PresetCulture presetCulture: presetCulturesList)
        {
            presetCulturesMap.put(presetCulture.getName().toLowerCase(), presetCulture);
        }
    }

    /**
     * If Preset Cultures is enabled, ensure all towns have a valid preset culture.
     */
    public static void sanitizeTownCultures() {

        if (Settings.isPresetCulturesEnabled()) {
            {
                for (Town town : TownyAPI.getInstance().getTowns()) {
                    if (!TownMetaDataController.hasTownCulture(town) || !PresetCulturesUtil.isPresetCulture(TownMetaDataController.getTownCulture(town))) {
                        PresetCulturesUtil.pickCultureAutomatically(town);
                    }
                }
            }
        }
    }

    public static PresetCulture findPresetCultureAtTownLocation(Town town)
    {
        return findPresetCultureAtCoord(town.getHomeBlockOrNull().getCoord());
    }

    public static PresetCulture findPresetCultureAtCoord(Coord coord)
    {
        for(PresetCulture presetCulture: presetCulturesMap.values())
        {
            if(coord.getX() >= presetCulture.getTopLeftCoord().getX()
                    && coord.getZ() >= presetCulture.getTopLeftCoord().getZ()
                    && coord.getX() <= presetCulture.getBottomRightCoord().getX()
                    && coord.getZ() <= presetCulture.getBottomRightCoord().getZ()) {
                return presetCulture;
            }
        }
        throw new RuntimeException("No cultural region found for coord " + coord.toString());
    }

    public static PresetCulture pickRandomPresetCulture() {
        List<PresetCulture> presetCulturesList = new ArrayList<>(presetCulturesMap.values());
        int pickedIndex = (int)(Math.random() * presetCulturesList.size());
        return presetCulturesList.get(pickedIndex);
    }

    public static boolean isPresetCulture(String cultureNameToTest) {
        return presetCulturesMap.containsKey(cultureNameToTest.toLowerCase());
    }

    public static Set<String> getPresetCultureNamesAsSet() {
        Set<String> result = new HashSet<>();
        for(PresetCulture presetCulture: presetCulturesMap.values())
        {
            result.add(presetCulture.getName());
        }
        return result;
    }

    public static PresetCulture getPresetCulture(String cultureName) {
        return presetCulturesMap.get(cultureName.toLowerCase());
    }

    public static void pickCultureAutomatically(Town town) {
        switch(Settings.getAutomaticCultureSelectionType()) {
            case RANDOM: {
                TownMetaDataController.setTownCulture(town, pickRandomPresetCulture().getName());
                return;
            }
            case LOCATION: {
                TownMetaDataController.setTownCulture(town, findPresetCultureAtTownLocation(town).getName());
                return;
            }
            default:
                throw new RuntimeException("Unknown enum for new town culture selection method.");
        }
    }

    @Nullable
    public static String findCorrectlyCapitalizedCultureName(String cultureNameWithUnreliableCapitalization) {
        if(presetCulturesMap.containsKey(cultureNameWithUnreliableCapitalization.toLowerCase()))
            return presetCulturesMap.get(cultureNameWithUnreliableCapitalization.toLowerCase()).getName();
        else 
            return null;
    }
}
