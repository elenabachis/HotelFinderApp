package HotelFinder.app.model;

import lombok.Getter;

@Getter
public class MetersPerDegree {
    private final double latitude;
    private final double longitude;

    public MetersPerDegree(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MetersPerDegree{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
