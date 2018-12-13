--najdi najblizsie skoly podla zvoleneho filtra
select point.name, st_asgeojson(ST_Transform(point.way,4326)), round(cast(ST_Distance(ST_MakePoint(-87.6530, 41.8725)::geography, 
ST_Transform(point.way, 4326)::geography) as numeric),0) 
as distance from planet_osm_point point where point.amenity in ('school') and 
ST_DWithin((ST_MakePoint(-87.6530, 41.8725))::geography, ST_Transform(point.way, 4326)::geography, 1000) 
ORDER BY distance limit 10

--najdi najblizsie skoly a vypocitaj danger level
SELECT name,geopoint,crime_count,distance FROM
(with nearbySchools as(
SELECT *, st_asgeojson(ST_Transform(point.way,4326)) as geopoint, 
round(cast(ST_Distance(ST_MakePoint(-87.6530, 41.8725)::geography,ST_Transform(point.way,4326)::geography, false) as numeric), 0)
as distance
FROM planet_osm_point point
WHERE st_dwithin(ST_MakePoint(-87.6530, 41.8725)::geography,ST_Transform(point.way,4326)::geography, 1000) AND point.amenity in ('school')
ORDER BY distance)
SELECT *, (SELECT COUNT(*) FROM crimes c WHERE st_dwithin(ST_Transform(c.way,4326)::geography, ST_Transform(point.way,4326)::geography, 1000)) as crime_count
FROM nearbySchools point) alias ORDER BY crime_count LIMIT 10

--najdli najblizsiu zastavku pri skole
select point.name, st_asgeojson(ST_Transform(point.way,4326)),
round(cast(ST_Distance(ST_MakePoint(-87.6530, 41.8725)::geography, ST_Transform(point.way, 4326)::geography) as numeric),0) as distance
from planet_osm_point point where point.highway in ('bus_stop')
and ST_DWithin((ST_MakePoint(-87.6530, 41.8725))::geography, ST_Transform(r.way, 4326)::geography, 5000) ORDER BY distance limit 1

--vsetky crimes za rok 2016
Select primary_type, st_asgeojson(ST_Transform(way,4326)) from crimes where crime_year = 2016 ORDER BY st_asgeojson

--Indexy:
CREATE INDEX index_amenity_points ON planet_osm_point(amenity);
CREATE INDEX index_highway_points ON planet_osm_point(highway);
CREATE INDEX index_type_crimes ON crimes(primary_type);