package HotelFinder.app.controller;

import HotelFinder.app.model.MetersPerDegree;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {

//    @GetMapping("/welcome")
//    public String welcome(Model model) {
//        model.addAttribute("name", "User");
//        return "welcome"; // refers to welcome.html in the templates folder
//    }

    @GetMapping("/geolocation")
    public String showGeolocationPage() {
        System.out.println("hello");
        return "geolocation.html"; // This refers to the geolocation.html template
    }


    @PostMapping("/submitGeolocation")
    public String submitGeolocation(@RequestParam("latitude") Double latitude,
                                    @RequestParam("longitude") Double longitude,
                                    @RequestParam("radius") Integer radius) {
        // Handle the submitted geolocation.html data here
        System.out.println("radius " + radius);
        System.out.println("Received latitude: " + latitude);
        System.out.println("Received longitude: " + longitude);
        System.out.println(distance(calculateMetersPerDegree(latitude,longitude),calculateMetersPerDegree(46.764654,23.598674)));
        System.out.println(calculateHaversineDistance(latitude,longitude, 46.764654252624204, 23.598674125224626));
        return "geolocation.html";
    }


    public MetersPerDegree calculateMetersPerDegree(double latitude, double longitude) {
        double phi = Math.toRadians(latitude); // Convert latitude to radians
        double phi_long = Math.toRadians(longitude);
        double metersPerDegreeLatitude = 111132.92 - 559.82 * Math.cos(2 * phi) + 1.175 * Math.cos(4 * phi) - 0.0023 * Math.cos(6 * phi);
        double metersPerDegreeLongitude = 111412.84 * Math.cos(phi_long) - 93.5 * Math.cos(3 * phi_long) + 0.118 * Math.cos(5 * phi_long);
        //double metersPerDegreeLongitude = 111412.84 * Math.cos(phi) - 93.5 * Math.cos(3 * phi) + 0.118 * Math.cos(5 * phi);
        return new MetersPerDegree(metersPerDegreeLatitude, metersPerDegreeLongitude);
    }


    public double distance(MetersPerDegree point1, MetersPerDegree point2){
        System.out.println(point1);
        System.out.println(point2);
        return Math.sqrt(Math.pow(point2.getLatitude() - point1.getLatitude(), 2) + Math.sqrt(Math.pow(point2.getLongitude() - point1.getLongitude(), 2)));
    }


    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371e3; // Radius of the Earth in meters
        double phi_lat_1 = Math.toRadians(lat1); // Convert latitude 1 to radians
        double phi_lat_2 = Math.toRadians(lat2); // Convert latitude 2 to radians
        double phi_lat_diff = Math.toRadians(lat2 - lat1); // Difference in latitude
        double phi_long_diff = Math.toRadians(lon2 - lon1); // Difference in longitude

        // Haversine formula
        double a = Math.sin(phi_lat_diff / 2) * Math.sin(phi_lat_diff / 2) +
                Math.cos(phi_lat_1) * Math.cos(phi_lat_2) *
                        Math.sin(phi_long_diff / 2) * Math.sin(phi_long_diff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c; // Distance in meters
        return d/1000;
    }

}
