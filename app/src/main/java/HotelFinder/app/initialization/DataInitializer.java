package HotelFinder.app.initialization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.example.configuration.DbConfigurator;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class DataInitializer {
    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD)) {

            BufferedReader reader = new BufferedReader(new FileReader("C://Users//elena//IdeaProjects//HotelApp//src//main//java//hotels.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();


            JSONArray hotelsArray = new JSONArray(jsonString.toString());


            String hotelSQL = "INSERT INTO hotels (id, name, latitude, longitude) VALUES (?, ?, ?, ?)";
            String roomSQL = "INSERT INTO rooms (roomNumber, hotel_id, type, price, isAvailable) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement hotelStmt = connection.prepareStatement(hotelSQL);
            PreparedStatement roomStmt = connection.prepareStatement(roomSQL);

            for (int i = 0; i < hotelsArray.length(); i++) {
                JSONObject hotel = hotelsArray.getJSONObject(i);
                int hotelId = hotel.getInt("id");
                String name = hotel.getString("name");
                double latitude = hotel.getDouble("latitude");
                double longitude = hotel.getDouble("longitude");


                hotelStmt.setInt(1, hotelId);
                hotelStmt.setString(2, name);
                hotelStmt.setDouble(3, latitude);
                hotelStmt.setDouble(4, longitude);
                hotelStmt.executeUpdate();


                JSONArray roomsArray = hotel.getJSONArray("rooms");
                for (int j = 0; j < roomsArray.length(); j++) {
                    JSONObject room = roomsArray.getJSONObject(j);
                    int roomNumber = room.getInt("roomNumber");
                    int type = room.getInt("type");
                    int price = room.getInt("price");
                    boolean isAvailable = room.getBoolean("isAvailable");

                    roomStmt.setInt(1, roomNumber);
                    roomStmt.setInt(2, hotelId);
                    roomStmt.setInt(3, type);
                    roomStmt.setInt(4, price);
                    roomStmt.setBoolean(5, isAvailable);
                    roomStmt.executeUpdate();
                }
            }

            hotelStmt.close();
            roomStmt.close();

            System.out.println("Data inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
