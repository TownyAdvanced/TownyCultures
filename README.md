# TownyCultures

INTRODUCTION
- TownyCultures is an add on plugin for Towny, which enables "cultures" on Towny servers.
 
FEATURES
- Each town has a 'culture' e.g. "Celtic", "Roman", "Azurian" etc.
- Each town culture is displayed on the Town screen.
- Town residents can quickly communicate with other town residents (in any town) 
  of the same culture, using the Culture Channel: `/cc <message>`.
- Town culture is fluid and dynamic. 
  There is no central administrator for each culture.
  A new culture can be created by a mayor, using: `/c set <culture>`".
  The same command can be used to join an existing culture.
- Nations do not create or specify culture in this way.
  Instead their culture is the sum of whichever cultures 
  their component towns identify with, 
  and/or whichever culture(s) they might claim to be affiliated with.
- Culture is important in wartime,
  because although a town might be captured by an enemy nation,
  the residents can continue communicating with their friends in the same culture,
  using the Culture Channel: `/cc <message>`.

COMMANDS

    PLAYER
    - `\c set culture` - As a mayor, have the town create or join a culture
    - `\cc <message>` - As a town resident, talk to others in the same culture

    ADMIN
    - `\ca reload` - Reload the plugin configs & language files
    - '\ca alltowns set culture <culture> - Set a culture for all towns
    - '\ca town <town> set culture <culture> - Set a culture for 1 town
      
INSTALLATION STEPS
1. Download the TownyCultures jar file here: https://github.com/TownyAdvanced/TownyCultures/releases
2. Drop the jar file into your normal plugins folder.
3. Stop your server.
4. Start your server.
5. Edit your townyperms.yml file in the Towny/settings folder, 
   and add the following permission for mayors (& other ranks if appropriate):
   `- townycultures.set_town_culture`.
6. Run the command `/ta reload townyperms`.   
7. That's it.
