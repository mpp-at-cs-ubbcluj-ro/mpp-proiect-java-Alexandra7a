package org.example.repository.interfaces;

import org.example.model.Reservation;

public interface ReservationRepository extends Repository<Long, Reservation> {

    public int getAllReservationsAt(Long id);

}
