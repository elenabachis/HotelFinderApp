package HotelFinder.app.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservationWRows {
    private int id;
    private int userId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private List<ReservationRow> rows;
    private int totalPrice;
    private Feedback feedback;

    public ReservationWRows(int id, int userId, LocalDateTime checkIn, LocalDateTime checkOut, List<ReservationRow> rows, int totalPrice, Feedback feedback) {
        this.id = id;
        this.userId = userId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rows = rows;
        this.totalPrice = totalPrice;
        this.feedback = feedback;
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

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
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


