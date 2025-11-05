package com.gmail.goosius.townycultures.enums;

public enum AutomaticCultureSelectionType {
    RANDOM, LOCATION;

    public static AutomaticCultureSelectionType parseText(String text) {
        switch (text.toUpperCase()) {
            case "RANDOM":
                return RANDOM;
            case "LOCATION":
                return LOCATION;
            default:
                throw new RuntimeException("Unrecognized enum text for AutomaticSelectionType:" + text);
        }
    }
}
