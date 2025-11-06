# TownyCultures

## Introduction
- TownyCultures is an add-on plugin for Towny, which enables "cultures" for Towns.
- Town residents that share a culture can use the TownyChat "Culture" channel to chat with each other.
  
## Features
- Each town has a 'culture' e.g. "Greek", "Celtic", "Azurian" etc.
- The town culture is displayed on the Town Screen.
- Town residents can communicate with all other town residents of the same culture by using the Culture Channel: `/cc <message>`.
- If the "Preset Cultures" feature is *disabled*, new cultures names can be created by mayors, simply by running `/town set culture <culture>`.
  <br>The same command can be used to join an existing culture.
- If the "Preset Cultures" feature is *enabled*, cultures names and descriptions are confured by the server, and can be based on the game-world location of the town e.g.
   <img width="4538" height="2586" alt="mapped_cultures_example_png" src="https://github.com/user-attachments/assets/184e2f59-617b-4fb9-a521-ea5d6bb79a19" />
- Nations do not technically have their own culture, but rather their culture is the sum of whichever cultures their towns are part of.
- Culture can be shown on dynmap town popups.
- Culture can be used as a PAPI placeholder.

## Commands
```
PLAYER
- `/cc <message>` - As a town resident, talk to others in the same culture
- `/town set culture <culture>` - As a mayor, have the town create or join a culture

ADMIN
- `/ta reload townycultures` - Reload the plugin configs & language files
- '/ta culture alltowns set culture <culture> - Set a culture for all towns
- '/ta culture town <town> set culture <culture> - Set a culture for 1 town
- '/ta culture culturelist - View a book listing the cultures on the server.
- '/ta culture deleteculture <culture> - Deletes the specified culture from the server.
```   

## Installation
1. Download the TownyCultures jar file here: https://github.com/TownyAdvanced/TownyCultures/releases.
2. Stop your server.
3. Drop the jar file into your server's plugins folder.
4. If you wish to enable the TownyChat culture channel, paste this into your Towny channels.yml file:
```
  culture:
    commands: cc
    type: DEFAULT
    channeltag: '&f[&5CC&f]'
    messagecolour: '&d'
    permission: towny.chat.culture
    range: '-1'
    spam_time: '0.5'
    hooked: true
```
5. Start your server.
6. If you wish to use the 'Preset Cultures' feature:
   - Edit your TownyCultures config.yml file, and set preset_cultures.enabled to true.
   - Edit your TownyCultures config.yml file, and set automatic_culture_selection_type as you wish.
   - Edit your TownyCultures config.yml file, and configure the list of preset cultures as you wish. Each culture entry is in the following form: `[top left x, top left y], [bottom right x, bottom right y], name, description`.
   - Run this command: `/ta reload townycultures`.
7. If you wish to give mayors the ability to set their town culture:
   - Run this command: `/ta townyperms group towns.mayor addperm townycultures.set_town_culture`.
8. To add culture to dynmap town popups, add %culture% to Dynmap-Towny config's InfoWindow.
9. To use culture as a PAPI placeholder, use %townycultures_culture%
10. That's it.

## Permissions
- townycultures.set_town_culture:
  - description: User is able to set town culture. (usually town mayors or assistants)
  - default: op

- townycultures.admin:
  - description: User is able to use all TownyCulture admin commands.
  - default: op
  - children:
    - townycultures.admin.reload: true
    - townycultures.admin.culturelist: true
    - townycultures.admin.deleteculture: true
    - townycultures.admin.alltowns: true
    - townycultures.admin.town: true

## Events
- PreCultureSetEvent - Cancellable event thrown before a Town sets their culture.

## PAPI Placeholders
- `%townycultures_culture%` - Shows the player's culture. 
