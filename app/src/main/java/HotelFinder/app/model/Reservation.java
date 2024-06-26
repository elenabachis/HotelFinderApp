package HotelFinder.app.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reservation {
    private int id;
    private int userId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public Reservation(int id, int userId, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.userId = userId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}
