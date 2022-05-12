package com.gmail.goosius.townycultures.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;

public class PreCultureSetEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final String culture;
	private final String townName;
	private final Town town;

	private boolean isCancelled = false;
	private String cancelMessage = Translatable.of("msg_err_command_disable").defaultLocale();
	
	public PreCultureSetEvent(String culture, Town town) {
		this.culture = culture;
		this.town = town;
		this.townName = town.getName();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public String getCultureName() {
		return culture;
	}

	public String getTownName() {
		return townName;
	}

	public Town getTown() {
		return town;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public String getCancelMessage() {
		return cancelMessage;
	}

	public void setCancelMessage(String cancelMessage) {
		this.cancelMessage = cancelMessage;
	}
}
