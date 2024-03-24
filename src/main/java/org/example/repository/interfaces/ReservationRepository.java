package org.example.repository.interfaces;

import org.example.model.Reservation;
import org.example.repository.interfaces.Repository;

public interface ReservationRepository extends Repository<Long, Reservation> {

    public int getAllReservationsAt(Long id);

}
