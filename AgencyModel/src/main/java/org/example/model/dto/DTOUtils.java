package org.example.model.dto;

import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Trip;


public class DTOUtils {
    public static Employee getFromDTO(EmployeeDTO emdto){
        String id=emdto.getUsername();
        String pass=emdto.getPassword();
        return new Employee(id, pass);
    }
    public static EmployeeDTO getDTO(Employee employee){
        String id=employee.getUsername();
        String pass=employee.getPassword();
        return new EmployeeDTO(id, pass);
    }

       public static TripDTO getDTO(Trip trip){
        TripDTO tripDTO=new TripDTO(trip.getPlace(), trip.getTransportCompanyName(), trip.getDeparture(), trip.getPrice(),trip.getTotalSeats());
        tripDTO.setId(trip.getId());
        return tripDTO;
    }
    public static Trip getFromDTO(TripDTO tripDTO){
        Trip trip = new Trip(tripDTO.getPlace(), tripDTO.getTransportCompanyName(), tripDTO.getDeparture(), tripDTO.getPrice(),tripDTO.getTotalSeats());
        trip.setId(tripDTO.getId());
        return trip;
    }

    public static Client getFromDTO(ClientDTO clientDTO){
        Client client=new Client(clientDTO.getName(),clientDTO.getBirthDate());
        client.setId(clientDTO.getId());
        return client;
    }
    public static ClientDTO getDTO(Client client){
        ClientDTO clientDTO=new ClientDTO(client.getName(),client.getBirthDate());
        clientDTO.setId(client.getId());
        return clientDTO;
    }
}
