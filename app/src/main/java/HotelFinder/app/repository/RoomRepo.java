package HotelFinder.app.repository;


import HotelFinder.app.model.Room;
import HotelFinder.app.configuration.DbConfigurator;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepo {

    // SQL queries
    private static final String INSERT_ROOM_SQL = "INSERT INTO Rooms (roomNumber, type, price, hotel_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ROOM_BY_ID_SQL = "SELECT * FROM Rooms WHERE room_id = ?";
    private static final String SELECT_ALL_ROOMS_SQL = "SELECT * FROM Rooms";
    private static final String UPDATE_ROOM_SQL = "UPDATE Rooms SET roomNumber = ?, type = ?, price = ?, hotel_id = ? WHERE room_id = ?";
    private static final String DELETE_ROOM_SQL = "DELETE FROM Rooms WHERE room_id = ?";
    private static final String SELECT_ALL__ROOMS_HOTELID_SQL = "SELECT * FROM Rooms WHERE hotel_id = ?";


    public void createRoom(Room room) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROOM_SQL)) {
            preparedStatement.setInt(1, room.getNumber());
            preparedStatement.setInt(2, room.getType());
            preparedStatement.setDouble(3, room.getPrice());
            preparedStatement.setInt(4, room.getHotel_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Room getRoomById(int id) {
        Room room = null;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                room = new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getInt("roomNumber"),
                        resultSet.getInt("type"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("hotel_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ROOMS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getInt("roomNumber"),
                        resultSet.getInt("type"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("hotel_id")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void updateRoom(Room room) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROOM_SQL)) {
            preparedStatement.setInt(1, room.getNumber());
            preparedStatement.setInt(2, room.getType());
            preparedStatement.setDouble(3, room.getPrice());
            preparedStatement.setInt(4, room.getHotel_id());
            preparedStatement.setInt(5, room.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(int id) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROOM_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Room> getAllAvailableRoomsHotel(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL__ROOMS_HOTELID_SQL)) {
            preparedStatement.setInt(1, hotelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getInt("roomNumber"),
                        resultSet.getInt("type"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("hotel_id")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }


}

