# TownyCultures

## Introduction
- TownyCultures is an add-on plugin for Towny, which enables "cultures" on Towny servers.
- Players that share a culture have their own "chat" channel.
- Towns choose what culture they want to be a part of.

## Features
- Each town has a 'culture' e.g. "Greek", "Celtic", "Roman", "Azurian" etc.
- Each town culture is displayed on the Town screen.
- Town residents can communicate with all other town residents of the same culture (regardless of nation),
  <br>using the Culture Channel: `/cc <message>`.
- Town culture is fluid and dynamic. 
  There is no central administrator for each culture.
  <br>A new culture can be created by a mayor, using: `/town set culture <culture>`.
  <br>The same command can be used to join an existing culture.
- Nations do not create or specify culture in this way.
  <br>Instead their culture is the sum of whichever cultures their component towns identify with, 
  <br>and/or whichever culture(s) they might claim to be affiliated with.
- Culture is important in war systems where town transfer-after-invasion is possible,
  <br>because although a town might be transferred to an enemy nation,
  <br>the residents can continue communicating with their friends in the same culture,
  <br>using the Culture Channel: `/cc <message>`.
- Culture can be shown on your dynmap' town popups.
  <br>Add %culture% in your Dynmap-Towny config's InfoWindow.
- Culture can be shown using the placeholder: %townycultures_culture%

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
1. Download the TownyCultures jar file here: https://github.com/TownyAdvanced/TownyCultures/releases
2. Stop your server.
3. Drop the jar file into your server's plugins folder.
4. To enable the culture channel, copy this into your Towny channels.yml file:
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
6. Give mayors the ability to set their town culture using this command:
```
/ta townyperms group towns.mayor addperm townycultures.set_town_culture
```
7. That's it.

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