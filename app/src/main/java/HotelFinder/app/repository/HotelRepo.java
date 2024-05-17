package org.example.repository;

import org.example.configuration.DbConfigurator;
import org.example.model.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepo {

    private static final String INSERT_HOTEL_SQL = "INSERT INTO Hotel (id, name, latitude, longitude) VALUES (?, ?, ?, ?)";
    private static final String SELECT_HOTEL_BY_ID_SQL = "SELECT * FROM Hotel WHERE id = ?";
    private static final String SELECT_ALL_HOTELS_SQL = "SELECT * FROM Hotel";
    private static final String UPDATE_HOTEL_SQL = "UPDATE Hotel SET name = ?, latitude = ?, longitude = ? WHERE id = ?";
    private static final String DELETE_HOTEL_SQL = "DELETE FROM Hotel WHERE id = ?";

    public void createHotel(Hotel hotel) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_HOTEL_SQL)) {
            preparedStatement.setInt(1, hotel.getId());
            preparedStatement.setString(2, hotel.getName());
            preparedStatement.setDouble(3, hotel.getLatitude());
            preparedStatement.setDouble(4, hotel.getLongitude());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Hotel getHotelById(int id) {
        Hotel hotel = null;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_HOTEL_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hotel = new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotel;
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_HOTELS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Hotel hotel = new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude")
                );
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    public void updateHotel(Hotel hotel) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_HOTEL_SQL)) {
            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setDouble(2, hotel.getLatitude());
            preparedStatement.setDouble(3, hotel.getLongitude());
            preparedStatement.setInt(4, hotel.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHotel(int id) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_HOTEL_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
