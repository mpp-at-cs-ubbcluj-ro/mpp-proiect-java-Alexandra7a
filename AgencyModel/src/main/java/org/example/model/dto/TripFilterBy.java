package org.example.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TripFilterBy implements Serializable {

    String placeToVisit;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public TripFilterBy(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime) {
        this.placeToVisit = placeToVisit;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPlaceToVisit() {
        return placeToVisit;
    }

    public void setPlaceToVisit(String placeToVisit) {
        this.placeToVisit = placeToVisit;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
