package org.example.model;

public class Feedback {
    private int id;
    private int reservation_id;
    private int servicesRating;
    private int cleanlinessRating;
    private String comments;

    public Feedback(int id, int reservation_id, int servicesRating, int cleanlinessRating, String comments) {
        this.id = reservation_id;
        this.reservation_id = reservation_id;
        this.servicesRating = servicesRating;
        this.cleanlinessRating = cleanlinessRating;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public int getid() {
        return reservation_id;
    }

    public void setreservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getServicesRating() {
        return servicesRating;
    }

    public void setServicesRating(int servicesRating) {
        this.servicesRating = servicesRating;
    }

    public int getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(int cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", reservation_id=" + reservation_id +
                ", servicesRating=" + servicesRating +
                ", cleanlinessRating=" + cleanlinessRating +
                ", comments='" + comments + '\'' +
                '}';
    }
}
