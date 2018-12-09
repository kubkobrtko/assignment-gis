# General course assignment

Build a map-based application, which lets the user see geo-based data on a map and filter/search through it in a meaningfull way. Specify the details and build it in your language of choice. The application should have 3 components:

1. Custom-styled background map, ideally built with [mapbox](http://mapbox.com). Hard-core mode: you can also serve the map tiles yourself using [mapnik](http://mapnik.org/) or similar tool.
2. Local server with [PostGIS](http://postgis.net/) and an API layer that exposes data in a [geojson format](http://geojson.org/).
3. The user-facing application (web, android, ios, your choice..) which calls the API and lets the user see and navigate in the map and shows the geodata. You can (and should) use existing components, such as the Mapbox SDK, or [Leaflet](http://leafletjs.com/).

## Example projects

- Showing nearby landmarks as colored circles, each type of landmark has different circle color and the more interesting the landmark is, the bigger the circle. Landmarks are sorted in a sidebar by distance to the user. It is possible to filter only certain landmark types (e.g., castles).

- Showing bicykle roads on a map. The roads are color-coded based on the road difficulty. The user can see various lists which help her choose an appropriate road, e.g. roads that cross a river, roads that are nearby lakes, roads that pass through multiple countries, etc.

## Data sources

- [Open Street Maps](https://www.openstreetmap.org/)

## Bezpečné školy

**Popis aplikácie**: Aplikácia bude hľadať bezpečné základné, stredné školy a vysoké školy a škôlky. Bude obsahovať tieto scenáre:
 1. Aplikácia bude slúžiť na vyhľadanie základných, stredných, vysokých škôl a škôlok vo vybranej oblasti Chicaga s možnosťou nastavenia radiusu hľadania a maximálneho počtu výsledkov zoradených podľa vzdialenosti. 
 2. Bude poskytovaná možnosť zobrazenia nebezpečnosti okolia školy podľa dát kriminality v danej oblasti (cez heat mapu, dáta kriminality boli upravené aby vyhovovali na daný problém škôl, teda boli odfiltrované nočné údaje a niektoré typy kriminality, ktoré sa netýkajú detí).
 3. Zobrazí školy aj s indikátorom nebezpečenstva školy z pohľadu kriminality v okolí školy, zoradené od najbezpečnejšej.
 4. Pre vybranú školú zobrazí najbližšiu autobusovú zastávku.

**Zdroje údajov**: - [Open Street Maps](https://www.openstreetmap.org/)
                   - [Chicago crime data](https://www.kaggle.com/chicago/chicago-crime)

**Použité technológie:** Java, Spring boot, thymeleaf, postgis, postgres, mapbox, boostrap.
