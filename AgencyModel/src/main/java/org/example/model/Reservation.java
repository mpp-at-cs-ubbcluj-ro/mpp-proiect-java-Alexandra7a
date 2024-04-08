package org.example.model;

public class Reservation extends Entity<Long> {
    private String clientName;
    private String phoneNumber;
    private int noSeats;
    private Trip trip;
    private Employee responsibleEmployee;
    private Client client;

    public Reservation(String clientName, String phoneNumber, int noSeats, Trip trip, Employee responsibleEmployee, Client client) {
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Employee getResponsibleEmployee() {
        return responsibleEmployee;
    }

    public void setResponsibleEmployee(Employee responsibleEmployee) {
        this.responsibleEmployee = responsibleEmployee;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
