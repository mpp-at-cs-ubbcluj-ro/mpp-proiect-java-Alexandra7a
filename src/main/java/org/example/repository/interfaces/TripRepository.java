package org.example.repository;

import org.example.model.Entity;
import org.example.model.Trip;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TripRepository extends Repository<Long,Trip> {

    public Iterable<Trip> findAllTripPlaceTime(String place, LocalDateTime startTime, LocalDateTime endTime );
}
