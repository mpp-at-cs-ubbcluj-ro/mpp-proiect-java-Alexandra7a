package org.example.repository.interfaces;

import org.example.model.Trip;
import org.example.repository.interfaces.Repository;

import java.time.LocalDateTime;

public interface TripRepository extends Repository<Long,Trip> {

    public Iterable<Trip> findAllTripPlaceTime(String place, LocalDateTime startTime, LocalDateTime endTime );
}
