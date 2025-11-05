package com.gmail.goosius.townycultures.objects;

import com.palmergames.bukkit.towny.object.Coord;

public class PresetCulture {
    private Coord topLeftCoord;
    private Coord bottomRightCoord;
    private String name;
    private String description;
    
    public PresetCulture(Coord topLeftCoord, Coord bottomRightCoord, String name, String description) {
        this.topLeftCoord = topLeftCoord;
        this.bottomRightCoord = bottomRightCoord;
        this.name = name;
        this.description = description;
    }

    public Coord getTopLeftCoord() {
        return topLeftCoord;
    }
    
    public Coord getBottomRightCoord() {
        return bottomRightCoord;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
