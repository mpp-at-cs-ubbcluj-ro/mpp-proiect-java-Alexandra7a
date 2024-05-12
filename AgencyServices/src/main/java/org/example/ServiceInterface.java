package org.example;

import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Trip;
import org.example.model.dto.ClientDTO;
import org.example.model.dto.ReservationDTO;
import org.example.model.dto.TripDTO;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ServiceInterface {

    public Iterable<TripDTO> getAllFilteredTripsPlaceTime(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime)throws AppException;

    public int getAllReservationsAt(Long id)throws AppException;
    public Iterable<TripDTO> getAllTrips() throws AppException;
    public Optional<Employee> findUser(String user, String pass,ObserverInterface client)throws AppException;
    public void logout(Employee employee,ObserverInterface client)throws AppException ;
    public Iterable<ClientDTO> getAllClients()throws AppException;
    public Optional<ReservationDTO> saveReservation(String clientName, String phoneNumber, int noSeats, Trip trip, Employee responsibleEmployee, Client client)throws AppException;


}
