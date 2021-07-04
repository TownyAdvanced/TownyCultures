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
	LANGUAGE(
			"language",
			"english.yml",
			"# The language file you wish to use"),
	TOWNY_CULTURES("townycultures",
			"",
			"#########################################################",
			"# +------------------------------------------------------+ #",
			"# |                 Towny Cultures settings                 | #",
			"# |                                                         |",
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
			"# The maximum number of characters a culture name can be.");

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
