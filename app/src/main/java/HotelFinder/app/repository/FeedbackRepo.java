package org.example.repository;

import org.example.configuration.DbConfigurator;
import org.example.model.Feedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepo {
    // SQL queries
    private static final String INSERT_FEEDBACK_SQL = "INSERT INTO feedback (id, reservation_id, services_rating, cleanliness_rating, comments) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FEEDBACK_BY_USER_HOTEL_SQL = "SELECT * FROM feedback WHERE id = ? AND reservation_id = ?";
    private static final String SELECT_ALL_FEEDBACKS_SQL = "SELECT * FROM feedback";
    private static final String UPDATE_FEEDBACK_SQL = "UPDATE feedback SET services_rating = ?, cleanliness_rating = ?, comments = ? WHERE id = ? AND reservation_id = ?";
    private static final String DELETE_FEEDBACK_SQL = "DELETE FROM feedback WHERE id = ? AND reservation_id = ?";

    public void createFeedback(Feedback feedback) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FEEDBACK_SQL)) {
            preparedStatement.setInt(1, feedback.getid());
            preparedStatement.setInt(2, feedback.getid());
            preparedStatement.setInt(3, feedback.getServicesRating());
            preparedStatement.setInt(4, feedback.getCleanlinessRating());
            preparedStatement.setString(5, feedback.getComments());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Feedback getFeedbackByUserAndHotel(int userId, int hotelId) {
        Feedback feedback = null;
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FEEDBACK_BY_USER_HOTEL_SQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, hotelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                feedback = new Feedback(
                        userId,
                        hotelId,
                        resultSet.getInt("services_rating"),
                        resultSet.getInt("cleanliness_rating"),
                        resultSet.getString("comments")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FEEDBACKS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("id"),
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("services_rating"),
                        resultSet.getInt("cleanliness_rating"),
                        resultSet.getString("comments")
                );
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public void updateFeedback(Feedback feedback) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FEEDBACK_SQL)) {
            preparedStatement.setInt(1, feedback.getServicesRating());
            preparedStatement.setInt(2, feedback.getCleanlinessRating());
            preparedStatement.setString(3, feedback.getComments());
            preparedStatement.setInt(4, feedback.getId());
            preparedStatement.setInt(5, feedback.getReservation_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFeedback(int userId, int hotelId) {
        try (Connection connection = DriverManager.getConnection(DbConfigurator.URL, DbConfigurator.USERNAME, DbConfigurator.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FEEDBACK_SQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, hotelId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
