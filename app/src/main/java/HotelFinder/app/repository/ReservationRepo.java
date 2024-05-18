package HotelFinder.app.repository;

import HotelFinder.app.model.Reservation;
import HotelFinder.app.configuration.DbConfigurator;
import HotelFinder.app.model.ReservationRow;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepo {
    // SQL queries
    private static final String INSERT_RESERVATION_SQL = "INSERT INTO Reservations (user_id, check_in, check_out) VALUES (?, ?, ?)";
    private static final String SELECT_RESERVATION_BY_ID_SQL = "SELECT * FROM Reservations WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS_SQL = "SELECT * FROM Reservations";
    private static final String UPDATE_RESERVATION_SQL = "UPDATE Reservations SET user_id = ?, check_in = ?, check_out = ? WHERE id = ?";
    private static final String DELETE_RESERVATION_SQL = "DELETE FROM Reservations WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS_ROW_ROOMID_SQL = "SELECT * FROM Reservations JOIN Reservations_row on Reservations.id=Reservations_row.reservation_id WHERE room_id=?";
    private static final String SELECT_LAST_AUTO_INCREMENT_SQL = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
    private static final String INSERT_RESERVATION_ROW_SQL = "INSERT INTO reservations_row (reservation_id, room_id) VALUES (?, ?)";

    private static final String SELECT_RESERVATION_ROW_BY_ID_SQL = "SELECT * FROM Reservations_row WHERE id = ?";
    private static final String SELECT_ALL_RESERVATIONS_USER_SQL = "SELECT * FROM Reservations WHERE user_id=?";
    private static final String SELECT_ALL_RESERVATIONS_ROW_RID_SQL = "SELECT * FROM Reservations_row WHERE reservation_id=?";
    private static final String DELETE_RESERVATION_ROWS_SQL = "DELETE FROM Reservations_row WHERE reservation_id = ?";

    public int createReservation(Reservation reservation) {
        String INSERT_RESERVATION_SQL = "INSERT INTO Reservations (user_id, check_in, check_out) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESERVATION_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, reservation.getUserId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(reservation.getCheckIn()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reservation.getCheckOut()));

            preparedStatement.executeUpdate();

            // Get the last inserted ID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int lastInsertedId = generatedKeys.getInt(1);
                    System.out.println("Last inserted ID: " + lastInsertedId);
                    return lastInsertedId;
                } else {
                    System.out.println("No records found after insertion");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
            preparedStatement.setTimestamp(2, Timestamp.valueOf(reservation.getCheckIn()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(reservation.getCheckOut()));
            preparedStatement.setInt(4, reservation.getId());
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


    public boolean isReservationOverlapping(int roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        boolean isOverlapping = false;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS_ROW_ROOMID_SQL)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LocalDateTime existingCheckIn = resultSet.getTimestamp("check_in").toLocalDateTime();
                LocalDateTime existingCheckOut = resultSet.getTimestamp("check_out").toLocalDateTime();

                if ((checkIn.isBefore(existingCheckIn) && checkOut.isAfter(existingCheckIn)) ||
                        (checkIn.isBefore(existingCheckOut) && checkOut.isAfter(existingCheckOut))) {
                    isOverlapping = true;
                    break;
                } else if (checkIn.isAfter(existingCheckIn) && checkOut.isBefore(existingCheckOut)) {
                    isOverlapping = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isOverlapping;
    }


    public int getLastAutoIncrementValue() {
        int lastAutoIncrementValue = -1;

        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LAST_AUTO_INCREMENT_SQL)) {

            preparedStatement.setString(1, connection.getCatalog());
            preparedStatement.setString(2, "reservations");

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lastAutoIncrementValue = resultSet.getInt("AUTO_INCREMENT");
            } else {
                System.out.println("Table 'reservations' not found or AUTO_INCREMENT not set.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(lastAutoIncrementValue);
        return lastAutoIncrementValue;
    }

    public void createReservationRow(ReservationRow reservationRow) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESERVATION_ROW_SQL)) {
            System.out.println(reservationRow);
            preparedStatement.setInt(1, reservationRow.getReservation_id());
            preparedStatement.setInt(2, reservationRow.getRoom_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservationsUser(int user_id) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS_USER_SQL)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
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

    public List<ReservationRow> getAllReservationsRowRID(int reservation_id) {
        List<ReservationRow> reservations = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_RESERVATIONS_ROW_RID_SQL)) {
            preparedStatement.setInt(1, reservation_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReservationRow reservationRow = new ReservationRow(
                        resultSet.getInt("id"),
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("room_id")
                );
                reservations.add(reservationRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void deleteReservationRows(int id) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESERVATION_ROWS_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReservationRow getReservationRowById(int reservationRowId) {
        ReservationRow reservation = null;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESERVATION_ROW_BY_ID_SQL)) {
            preparedStatement.setInt(1, reservationRowId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservation = new ReservationRow(
                        resultSet.getInt("id"),
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("room_id")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }
}
