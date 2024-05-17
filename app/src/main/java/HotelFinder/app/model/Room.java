package org.example.model;

public class Room {

    int id;
    int number;
    int type;
    double price;
    boolean isAvailable;
    int hotel_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number=" + number +
                ", type=" + type +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", hotel_id=" + hotel_id +
                '}';
    }

    public Room(int id, int number, int type, double price, boolean isAvailable, int hotel_id) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
        this.hotel_id = hotel_id;
    }
}
