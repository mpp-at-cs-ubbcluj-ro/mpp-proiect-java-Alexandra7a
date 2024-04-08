package org.example.server;


import org.example.model.dto.ClientDTO;
import org.example.model.dto.DTOUtils;
import org.example.model.dto.ReservationDTO;
import org.example.model.dto.TripDTO;
import org.example.model.*;
import org.example.ServiceInterface;
import org.example.ObserverInterface;
import org.example.AppException;
import org.example.repository.interfaces.ClientRepository;
import org.example.repository.interfaces.EmployeeRepository;
import org.example.repository.interfaces.ReservationRepository;
import org.example.repository.interfaces.TripRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class ServicesImplementation implements ServiceInterface {

    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;
    private EmployeeRepository employeeRepository;
    private ClientRepository clientRepository;
    private Map<String, ObserverInterface> loggedClients;

    public ServicesImplementation(TripRepository repo, ReservationRepository repo2,EmployeeRepository repo3,ClientRepository repo4) {
        this.tripRepository = repo;
        this.reservationRepository = repo2;
        this.employeeRepository = repo3;
        this.clientRepository = repo4;
        loggedClients=new ConcurrentHashMap<>();

    }

    public synchronized void logout(Employee user, ObserverInterface client) throws AppException {
        ObserverInterface localClient=loggedClients.remove(user.getUsername());
        if (localClient==null)
            throw new AppException("User "+user.getId()+" is not logged in.");
        //notifyFriendsLoggedOut(user);
    }

    private final int defaultThreadsNo=5;

    @Override
    public synchronized int getAllReservationsAt(Long id) throws AppException {
        return reservationRepository.getAllReservationsAt(id);
    }

    @Override
    public synchronized Iterable<TripDTO> getAllTrips() throws AppException {

        Iterable<Trip> trips= tripRepository.findAll();
        List<TripDTO> tripsDTO = new ArrayList<>();
        for(Trip trip: trips) {
            tripsDTO.add(DTOUtils.getDTO(trip));
        }
        return tripsDTO;
    }

    @Override
    public synchronized Optional<Employee> findUser(String user, String pass, ObserverInterface client) throws AppException {

      /*  //TODO: poate trebuie returnat DTO????????????????????
        Optional<Employee> result=employeeRepository.findOnebyUsername(user);
        if( result.isPresent() && result.get().getPassword().equals(pass))
            return result;
        return  Optional.empty();*/

        Optional<Employee> userR=employeeRepository.findOnebyUsername(user);
        if (userR.isPresent()&& userR.get().getPassword().equals(pass)){
            if(loggedClients.get(user)!=null)
                throw new AppException("User already logged in.");
            loggedClients.put(user, client);
            //notifyFriendsLoggedIn(user);
        }else
            throw new AppException("Authentication failed.");
        return  Optional.empty();
    }


}
