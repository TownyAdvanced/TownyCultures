package com.gmail.goosius.townycultures.settings;

public enum ConfigNodes {

	VERSION_HEADER("version", "", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version.  Please do not edit."),
	LAST_RUN_VERSION(
			"version.last_run_version",
			"",
			"# This is for showing the changelog on updates.  Please do not edit."),
	TOWNY_CULTURES("townycultures",
			"",
            "",
			"############################################################",
			"# +------------------------------------------------------+ #",
            "# |                                                      | #",
			"# |                 Towny Cultures settings              | #",
			"# |                                                      | #",
			"# +------------------------------------------------------+ #",
			"############################################################",
			""),
	TOWNY_CULTURES_ENABLED(
			"townycultures.enabled",
			"true",
			"",
			"# If this value is true, then TownyCultures is enabled."),
	MAXIMUM_NAME_LENGTH(
			"townycultures.maximum_culture_name_length",
			"20",
			"",
			"# The maximum number of characters a culture name can be."),
    PRESET_CULTURES("townycultures.preset_cultures",
            "",
            "",
            "# +------------------------------------------------------+ #",
            "# |                    Preset Cultures                   | #",
            "# +------------------------------------------------------+ #",
            ""),
    PRESET_CULTURES_ENABLED(
            "townycultures.preset_cultures.enabled",
            "false",
            "",
            "# If this value is true, then Preset Cultures are enabled."),
    PRESET_CULTURES_AUTOMATIC_CULTURE_SELECTION_TYPE(
            "townycultures.preset_cultures.automatic_culture_selection_type",
            "LOCATION",
            "",
            "# This value affects how preset cultures are automatically selected for towns. Allowed values:",
            "# RANDOM = If town does not have a preset culture, one is selected at random.",
            "# LOCATION = If town does not have a preset culture, one is selected based on town location."),
    PRESET_CULTURES_LIST(
            "townycultures.preset_cultures.list",
            "[-20000, -20000], [-6667, -6667], North-Western, The people of the North-West are scavengers and traders. | " +
            "[-6667, -20000], [6667, -6667], Northern, The people of the North are peaceful farmers. | " +
            "[6667, -20000], [20000, -6667], North-Eastern, The people of the North-East are savage and warlike. | " +
            "[-20000, -6667], [-6667, 6667], Western, The people of the West are ambitious empire builders. | " +
            "[-6667, -6667], [6667, 6667], Central, The people of the Center are hard working industrialists. | " +
            "[6667, -6667], [20000, 6667], Eastern, The people of the East are skilled merchants. | " +
            "[-20000, 6667], [-6667, 20000], South-Western, The people of the South-West are peaceful and spiritual. | " +
            "[-6667, 6667], [6667, 20000], Southern, The people of the South are barbarous and isolationist. | " +
            "[6667, 6667], [20000, 20000], South-Eastern, The people of the South-East are polite and diplomatic.",
            "",
            "# The list of preset cultures.");

	private final String Root;
	private final String Default;
	private String[] comments;

	ConfigNodes(String root, String def, String... comments) {

		this.Root = root;
		this.Default = def;
		this.comments = comments;
	}

	/**
	 * Retrieves the root for a config option
	 *
	 * @return The root for a config option
	 */
	public String getRoot() {

		return Root;
	}

	/**
	 * Retrieves the default value for a config path
	 *
	 * @return The default value for a config path
	 */
	public String getDefault() {

		return Default;
	}

	/**
	 * Retrieves the comment for a config path
	 *
	 * @return The comments for a config path
	 */
	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}

}
