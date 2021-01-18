package com.gmail.goosius.townycultures.enums;

/**
 * 
 * @author Goosius
 *
 */
public enum TownyCulturesPermissionNodes {

	// Command Nodes:
	TOWNYCULTURES_COMMAND_SET_TOWN_CULTURE("townycultures.set_town_culture"),
	TOWNYCULTURES_COMMAND_ADMIN("townycultures.admin.*"),
	TOWNYCULTURES_COMMAND_ADMIN_SET_SPECIFIED_TOWN_CULTURE("townycultures.admin.set_specified_town_culture"),
	TOWNYCULTURES_COMMAND_ADMIN_SET_ALL_TOWN_CULTURES("townycultures.admin.set_all_town_cultures");

	private String value;

	/**
	 * Constructor
	 * 
	 * @param permission - Permission.
	 */
	TownyCulturesPermissionNodes(String permission) {

		this.value = permission;
	}

	/**
	 * Retrieves the permission node
	 * 
	 * @return The permission node
	 */
	public String getNode() {

		return value;
	}

	/**
	 * Retrieves the permission node
	 * replacing the character *
	 * 
	 * @param replace - String
	 * @return The permission node
	 */
	public String getNode(String replace) {

		return value.replace("*", replace);
	}
}
