package org.example.controller;

import org.example.model.Feedback;
import org.example.repository.FeedbackRepo;

import java.util.List;

public class FeedbackController {
    private final FeedbackRepo feedbackRepo;

    public FeedbackController() {
        this.feedbackRepo = new FeedbackRepo();
    }

    public void createFeedback(Feedback feedback) {
        feedbackRepo.createFeedback(feedback);
    }

    public Feedback getFeedbackById(int userId, int hotelId) {
        return feedbackRepo.getFeedbackByUserAndHotel(userId, hotelId);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepo.getAllFeedbacks();
    }

    public void updateFeedback(Feedback feedback) {
        feedbackRepo.updateFeedback(feedback);
    }

    public void deleteFeedback(int userId, int hotelId) {
        feedbackRepo.deleteFeedback(userId, hotelId);
    }
}
