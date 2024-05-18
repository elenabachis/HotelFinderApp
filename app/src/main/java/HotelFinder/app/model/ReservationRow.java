package HotelFinder.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationRow {
    private int id;
    private int reservation_id;
    private int room_id;

    public ReservationRow(int id, int reservation_id, int room_id) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.room_id = room_id;
    }

    @Override
    public String toString() {
        return "ReservationRow{" +
                "id=" + id +
                ", reservation_id=" + reservation_id +
                ", room_id=" + room_id +
                '}';
    }
}
