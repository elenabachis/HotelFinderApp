package org.example.controller;

import org.example.model.Reservation;
import org.example.repository.ReservationRepo;

import java.util.List;

public class ReservationController {
    private final ReservationRepo reservationRepo;

    public ReservationController() {
        this.reservationRepo = new ReservationRepo();
    }

    public void createReservation(Reservation reservation) {
        reservationRepo.createReservation(reservation);
    }

    public Reservation getReservationById(int id) {
        return reservationRepo.getReservationById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepo.getAllReservations();
    }

    public void updateReservation(Reservation reservation) {
        reservationRepo.updateReservation(reservation);
    }

    public void deleteReservation(int id) {
        reservationRepo.deleteReservation(id);
    }
}
