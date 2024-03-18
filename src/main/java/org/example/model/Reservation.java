package org.example.model;

public class Reservation extends Entity<Long>{
    private String clientName;
    private String phoneNumber;
    private int noSeats;
    private int trip;
    private String responsibleEmployee;
    private int client;

    public Reservation(String clientName, String phoneNumber, int noSeats, int trip, String responsibleEmployee, int client) {
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.noSeats = noSeats;
        this.trip = trip;
        this.responsibleEmployee = responsibleEmployee;
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    public int getTrip() {
        return trip;
    }

    public void setTrip(int trip) {
        this.trip = trip;
    }

    public String getResponsibleEmployee() {
        return responsibleEmployee;
    }

    public void setResponsibleEmployee(String responsibleEmployee) {
        this.responsibleEmployee = responsibleEmployee;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }
}
