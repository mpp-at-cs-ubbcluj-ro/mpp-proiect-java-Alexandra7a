package org.example.service;

import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Reservation;
import org.example.model.Trip;
import org.example.repository.ClientDBRepository;
import org.example.repository.interfaces.ClientRepository;
import org.example.repository.interfaces.EmployeeRepository;
import org.example.repository.interfaces.ReservationRepository;
import org.example.repository.interfaces.TripRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class Service {
    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;
    private EmployeeRepository employeeRepository;
    private ClientRepository clientRepository;

    public Service(TripRepository repo, ReservationRepository repo2,EmployeeRepository repo3,ClientRepository repo4) {
        this.tripRepository = repo;
        this.reservationRepository = repo2;
        this.employeeRepository = repo3;
        this.clientRepository = repo4;
    }

    public Iterable<Trip> getAllTrips()
    {
        return tripRepository.findAll();
    }
    public Iterable<Trip> findAllTripPlaceTime(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime)
    {
        return tripRepository.findAllTripPlaceTime(placeToVisit,startTime,endTime);
    }
    Optional<Trip> getOne(Long id){
       return Optional.empty();
    }


    public int getAllReservationsAt(Long id) {
        return reservationRepository.getAllReservationsAt(id);
    }

    public Optional<Employee> findUser(String user, String pass) {
        Optional<Employee> result=employeeRepository.findOnebyUsername(user);
        if( result.isPresent() && result.get().getPassword().equals(pass))
            return result;
        return  Optional.empty();
    }

    public Iterable<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Reservation> reserveTicet(String clientName, String phoneNumber, int noSeats, Trip trip, Employee responsibleEmployee, Client client)
    {
        Reservation reservation=new Reservation(clientName,phoneNumber,noSeats,trip,responsibleEmployee,client);
        reservationRepository.save(reservation);
        return Optional.empty();
    }
}
