package HotelFinder.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Room {

    int id;
    int number;
    int type;
    double price;
    int hotel_id;


    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number=" + number +
                ", type=" + type +
                ", price=" + price +
                ", hotel_id=" + hotel_id +
                '}';
    }

    public Room(int id, int number, int type, double price, int hotel_id) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.price = price;
        this.hotel_id = hotel_id;
    }
}
