package HotelFinder.app.controller;

import HotelFinder.app.model.*;
import HotelFinder.app.repository.HotelRepo;
import HotelFinder.app.repository.ReservationRepo;
import HotelFinder.app.repository.RoomRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {


    @GetMapping("/geolocation")
    public String showGeolocationPage() {
//        System.out.println("hello");
        return "geolocation.html";
    }


    @PostMapping("/submitGeolocation")
    public String submitGeolocation(@RequestParam("latitude") Double latitude,
                                    @RequestParam("longitude") Double longitude,
                                    @RequestParam("radius") Double radius) {
//        System.out.println("radius " + radius);
//        System.out.println("Received latitude: " + latitude);
//        System.out.println("Received longitude: " + longitude);
//        System.out.println(distance(calculateMetersPerDegree(latitude, longitude), calculateMetersPerDegree(46.764654, 23.598674)));
//        System.out.println(calculateHaversineDistance(latitude, longitude, 46.764654252624204, 23.598674125224626));

        return "redirect:/reservationHotel?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius;
    }

    @GetMapping("/reservationHotel")
    public String reservationHotel(@RequestParam("latitude") Double latitude,
                                   @RequestParam("longitude") Double longitude,
                                   @RequestParam("radius") Double radius,
                                   Model model) {
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("radius", radius);
        HotelRepo repo = new HotelRepo();
        ArrayList<Hotel> hotels_in_range = new ArrayList<>();
        for (Hotel hotel : repo.getAllHotels()) {
            if (calculateHaversineDistance(latitude, longitude, hotel.getLatitude(), hotel.getLongitude()) < radius) {
                hotels_in_range.add(hotel);
            }
            model.addAttribute("hotels", hotels_in_range);

        }

        return "reservation_hotel";
    }

    @GetMapping("/selectHotel")
    public String selectHotel(@RequestParam("hotelId") Integer hotelId,
                              @RequestParam("checkInDate") @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm") LocalDateTime checkInDate,
                              @RequestParam("checkOutDate") @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm") LocalDateTime checkOutDate,
                              Model model) {

        RoomRepo repo = new RoomRepo();
        List<Room> rooms = repo.getAllAvailableRoomsHotel(hotelId);

        ReservationRepo reservationRepo = new ReservationRepo();
        List<Room> available_rooms = new ArrayList<>();

        for (Room room : rooms) {
            if (!reservationRepo.isReservationOverlapping(room.getId(), checkInDate, checkOutDate)) {
                available_rooms.add(room);
            }
        }

        model.addAttribute("rooms", available_rooms);


        return "reservation_rooms";
    }


    @PostMapping("/submitRooms")
    public String submitRooms(@RequestParam("selectedRoomIds") List<Integer> selectedRoomIds,
                              @RequestParam("checkInDate") LocalDateTime checkInDate,
                              @RequestParam("checkOutDate") LocalDateTime checkOutDate) {
        ReservationRepo reservationRepo = new ReservationRepo();
        int userId = 1;
        int reservationId = reservationRepo.createReservation(new Reservation(0, userId, checkInDate, checkOutDate));

        for (Integer roomId : selectedRoomIds) {
            reservationRepo.createReservationRow(new ReservationRow(0, reservationId, roomId));
        }

        return "redirect:/reservations";
    }

    @GetMapping("/reservations")
    public String showReservations(Model model) {
        ReservationRepo reservationRepo = new ReservationRepo();
        RoomRepo roomRepo = new RoomRepo();
        List<Reservation> reservations = reservationRepo.getAllReservations();
        List<ReservationWRows> final_reservation = new ArrayList<>();
        for (Reservation reservation : reservations) {
            List<ReservationRow> rows = reservationRepo.getAllReservationsRowRID(reservation.getId());
            int price = 0;
            for (ReservationRow reservationRow : rows) {
                price += (int) roomRepo.getRoomById(reservationRow.getRoom_id()).getPrice();
            }
            ReservationWRows reservationWRows = new ReservationWRows(reservation.getId(), reservation.getUserId(), reservation.getCheckIn(), reservation.getCheckOut(), rows, price);
            final_reservation.add(reservationWRows);
        }
        model.addAttribute("reservations", final_reservation);
        return "reservations";
    }

    @GetMapping("/modifyReservation")
    public String modifyReservation(@RequestParam("reservationId") int reservationId,
                                    @RequestParam("reservationRowId") int reservationRowId) {

        ReservationRepo reservationRepo = new ReservationRepo();
        RoomRepo roomRepo = new RoomRepo();
        Reservation reservation = reservationRepo.getReservationById(reservationId);

        int roomId = reservationRepo.getReservationRowById(reservationRowId).getRoom_id();
        int hotelId = roomRepo.getRoomById(roomId).getHotel_id();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String checkInDateStr = reservation.getCheckIn().format(formatter);
        String checkOutDateStr = reservation.getCheckOut().format(formatter);


        deleteReservation(reservationId);

        return "redirect:/selectHotel?hotelId=" + hotelId + "&checkInDate=" + checkInDateStr + "&checkOutDate=" + checkOutDateStr;
    }


    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("reservationId") int reservationId) {
        ReservationRepo reservationRepo = new ReservationRepo();
        reservationRepo.deleteReservationRows(reservationId);
        reservationRepo.deleteReservation(reservationId);
        return "redirect:/reservations";
    }


    public MetersPerDegree calculateMetersPerDegree(double latitude, double longitude) {
        double phi = Math.toRadians(latitude); // Convert latitude to radians
        double phi_long = Math.toRadians(longitude);
        double metersPerDegreeLatitude = 111132.92 - 559.82 * Math.cos(2 * phi) + 1.175 * Math.cos(4 * phi) - 0.0023 * Math.cos(6 * phi);
        double metersPerDegreeLongitude = 111412.84 * Math.cos(phi_long) - 93.5 * Math.cos(3 * phi_long) + 0.118 * Math.cos(5 * phi_long);
        //double metersPerDegreeLongitude = 111412.84 * Math.cos(phi) - 93.5 * Math.cos(3 * phi) + 0.118 * Math.cos(5 * phi);
        return new MetersPerDegree(metersPerDegreeLatitude, metersPerDegreeLongitude);
    }


    public double distance(MetersPerDegree point1, MetersPerDegree point2) {
//        System.out.println(point1);
//        System.out.println(point2);
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
        return d / 1000;
    }

}
