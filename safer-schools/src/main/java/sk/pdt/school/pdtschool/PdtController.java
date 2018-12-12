package sk.pdt.school.pdtschool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
final class PdtController {

  private final DatabaseQueries customClass;

  @Autowired
  public PdtController(DatabaseQueries customClass) {
    this.customClass = customClass;
  }

  @PostMapping("/getCrimes")
  public String getCrimes() {
    return customClass.getCrimesHeatMap();
  }

  @PostMapping("/getSchools")
  public String getSchools(@RequestBody SchoolFilter schoolFilter) {
    return customClass.getNearbySchools(schoolFilter);
  }

  @PostMapping("/getSchoolsWithCrimeCounts")
  public String getSchoolsWithCrimeCounts(@RequestBody SchoolFilter schoolFilter) {
    return customClass.getNearbySchoolWithRank(schoolFilter);
  }

  @PostMapping("/getNearestBusStop")
  public String getNearestBusStop(@RequestBody SchoolFilter schoolFilter) {
    return customClass.getNearestBusStop(schoolFilter);
  }
}

