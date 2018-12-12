package sk.pdt.school.pdtschool;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseQueries {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public DatabaseQueries(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  String getCrimesHeatMap() {
    return jdbcTemplate.query("Select primary_type, st_asgeojson(ST_Transform(way,4326)) " +
            "from crimes where crime_year = ? ORDER BY st_asgeojson",
        preparedStatement -> preparedStatement.setInt(1,2016),
        this::getGeoJson);
  }

  String getNearbySchools(SchoolFilter schoolFilter) {
    return jdbcTemplate.query("select point.name, st_asgeojson(ST_Transform(way,4326)),\n" +
        "point.amenity, round(cast(ST_Distance(ST_MakePoint(?, ?)::geography,\n" +
            "ST_Transform(point.way, 4326)::geography) as numeric),0)\n" +
            "as distance from planet_osm_point point where point.amenity in (?) and\n" +
            "ST_DWithin((ST_MakePoint(?, ?))::geography, ST_Transform(point.way, 4326)::geography, ?)\n" +
            "ORDER BY distance limit ?",
        preparedStatement -> {

          preparedStatement.setDouble(1, schoolFilter.getLongitude());
          preparedStatement.setDouble(2, schoolFilter.getLatitude());

          preparedStatement.setString(3, schoolFilter.getSchoolType());
          preparedStatement.setDouble(4, schoolFilter.getLongitude());
          preparedStatement.setDouble(5, schoolFilter.getLatitude());

          preparedStatement.setInt(6, schoolFilter.getRadius());
          preparedStatement.setInt(7, schoolFilter.getLimit());
        },
        this::getGeoJson);
  }
  String getNearbySchoolWithRank(SchoolFilter schoolFilter){
    return jdbcTemplate.query("SELECT name,geopoint,crime_count,distance FROM\n" +
            "(with nearbySchools as(\n" +
            "SELECT *, st_asgeojson(ST_Transform(point.way,4326)) as geopoint, \n" +
            "round(cast(ST_Distance(ST_MakePoint(?, ?)::geography,ST_Transform(point.way,4326)::geography, false) as numeric), 0)\n" +
            "as distance FROM planet_osm_point point\n" +
            "WHERE st_dwithin(ST_MakePoint(?, ?)::geography,ST_Transform(point.way,4326)::geography, 1000) AND point.amenity in (?)\n" +
            "ORDER BY distance)\n" +
            "SELECT *, (SELECT COUNT(*) FROM crimes c WHERE st_dwithin(ST_Transform(c.way,4326)::geography, ST_Transform(point.way,4326)::geography, 1000)) as crime_count\n" +
            "FROM nearbySchools point) alias ORDER BY crime_count LIMIT ?",
        preparedStatement -> {

          preparedStatement.setDouble(1, schoolFilter.getLongitude());
          preparedStatement.setDouble(2, schoolFilter.getLatitude());

          preparedStatement.setDouble(3, schoolFilter.getLongitude());
          preparedStatement.setDouble(4, schoolFilter.getLatitude());

          preparedStatement.setString(5, schoolFilter.getSchoolType());
          preparedStatement.setInt(6, schoolFilter.getLimit());
        },
        this::getCrimeGeoJson);
  }

  String getNearestBusStop(SchoolFilter schoolFilter){
    return jdbcTemplate.query("select point.name, st_asgeojson(ST_Transform(point.way,4326)),\n" +
            "round(cast(ST_Distance(ST_MakePoint(?, ?)::geography, ST_Transform(point.way, 4326)::geography) as numeric),0) as distance\n" +
            "from planet_osm_point point where point.highway in ('bus_stop')\n" +
            "and ST_DWithin((ST_MakePoint(?, ?))::geography, ST_Transform(point.way, 4326)::geography, 5000) ORDER BY distance limit 1",
        preparedStatement -> {

          preparedStatement.setDouble(1, schoolFilter.getLongitude());
          preparedStatement.setDouble(2, schoolFilter.getLatitude());

          preparedStatement.setDouble(3, schoolFilter.getLongitude());
          preparedStatement.setDouble(4, schoolFilter.getLatitude());
        },
        this::getGeoJson);
  }

  private String getGeoJson(ResultSet resultSet) throws SQLException {
    StringBuilder geoJson = new StringBuilder();
    geoJson.append("[");
    while (resultSet.next()) {
      geoJson.append("{")
          .append("\"type\": \"Feature\", ")
          .append("\"geometry\": ").append(resultSet.getString(2)).append(", ")
          .append("\"properties\": {")
          .append("\"title\": \"").append(resultSet.getString(1)).append("\"}},");
    }
    if (geoJson.length() > 1) {
      geoJson.deleteCharAt(geoJson.length() - 1);
    }
    geoJson.append("]");
    return geoJson.toString();
  }

  private String getCrimeGeoJson(ResultSet resultSet) throws SQLException {
    StringBuilder geoJson = new StringBuilder();
    geoJson.append("[");
    while (resultSet.next()) {
      geoJson.append("{")
          .append("\"type\": \"Feature\", ")
          .append("\"geometry\": ").append(resultSet.getString(2)).append(", ")
          .append("\"properties\": {")
          .append("\"crime_count\": ").append(resultSet.getInt(3)).append(", ")
          .append("\"title\": \"").append(resultSet.getString(1)).append("\"}},");
    }
    if (geoJson.length() > 1) {
      geoJson.deleteCharAt(geoJson.length() - 1);
    }
    geoJson.append("]");
    return geoJson.toString();
  }
}

