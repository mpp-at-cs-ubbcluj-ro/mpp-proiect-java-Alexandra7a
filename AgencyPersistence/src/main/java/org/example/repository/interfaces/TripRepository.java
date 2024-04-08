package org.example.repository.interfaces;

import org.example.model.Trip;

import java.time.LocalDateTime;

public interface TripRepository extends Repository<Long,Trip> {

    public Iterable<Trip> findAllTripPlaceTime(String place, LocalDateTime startTime, LocalDateTime endTime );
}
