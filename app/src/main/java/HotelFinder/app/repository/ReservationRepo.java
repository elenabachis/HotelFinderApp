package org.example.repository;

import org.example.configuration.DbConfigurator;
import org.example.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepo {
    // SQL queries
    private static final String INSERT_RESERVATION_SQL = "INSERT INTO Reservation (user_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?)";
    private static final String SELECT_RESERVATION_BY_ID_SQL = "SELECT * FROM Reservation WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS_SQL = "SELECT * FROM Reservation";
    private static final String UPDATE_RESERVATION_SQL = "UPDATE Reservation SET user_id = ?, room_id = ?, check_in = ?, check_out = ? WHERE id = ?";
    private static final String DELETE_RESERVATION_SQL = "DELETE FROM Reservation WHERE id = ?";

    public void createReservation(Reservation reservation) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESERVATION_SQL)) {
            preparedStatement.setInt(1, reservation.getUserId());
            preparedStatement.setInt(2, reservation.getRoomId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reservation.getCheckIn()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(reservation.getCheckOut()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESERVATION_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        resultSet.getTimestamp("check_out").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getTimestamp("check_in").toLocalDateTime(),
                        resultSet.getTimestamp("check_out").toLocalDateTime()
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void updateReservation(Reservation reservation) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESERVATION_SQL)) {
            preparedStatement.setInt(1, reservation.getUserId());
            preparedStatement.setInt(2, reservation.getRoomId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reservation.getCheckIn()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(reservation.getCheckOut()));
            preparedStatement.setInt(5, reservation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int id) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESERVATION_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
