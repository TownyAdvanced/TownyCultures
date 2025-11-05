package com.gmail.goosius.townycultures.listeners;

import com.gmail.goosius.townycultures.metadata.TownMetaDataController;
import com.gmail.goosius.townycultures.settings.Settings;
import com.gmail.goosius.townycultures.utils.CultureUtil;
import com.gmail.goosius.townycultures.utils.Messaging;
import com.palmergames.bukkit.TownyChat.events.AsyncChatHookEvent;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class TownyChatListener implements Listener {

    @EventHandler
    public void onTownyChat(AsyncChatHookEvent event) {
        //Verify player can use the culture channel
        if (!Settings.isTownyCulturesEnabled())
            return;
        if (!event.getChannel().getName().equalsIgnoreCase("culture"))
            return;
        Translator translator = Translator.locale(event.getPlayer());
        Resident resident = TownyAPI.getInstance().getResident(event.getPlayer());
        if (resident == null)
            return;
        if (!resident.hasTown()) {
            event.setCancelled(true);
            Messaging.sendErrorMsg(event.getPlayer(), translator.of("msg_err_cannot_use_cc_no_town"));
        }
        if(!TownMetaDataController.hasTownCulture(resident.getTownOrNull())) {
            event.setCancelled(true);
            Messaging.sendErrorMsg(event.getPlayer(), translator.of("msg_err_cannot_use_cc_no_town_culture"));
        }

        /*
         * Send the culture chat.
         * Filter the vanilla list of recipients
         */
        String chatterCulture = TownMetaDataController.getTownCulture(resident.getTownOrNull());
        Set<Player> filteredRecipientsList = new HashSet<>();
        for(Player unfilteredRecipient: event.getRecipients()) {
            if(CultureUtil.isSameCulture(unfilteredRecipient, chatterCulture))
                filteredRecipientsList.add(unfilteredRecipient);
        }
        event.setRecipients(filteredRecipientsList);
    }
}