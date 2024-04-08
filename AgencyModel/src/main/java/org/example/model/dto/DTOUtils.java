package org.example.model.dto;

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

   /* public static Message getFromDTO(MessageDTO mdto){
        Employee sender=new Employee(mdto.getSenderId());
        Employee receiver=new Employee(mdto.getReceiverId());
        String text=mdto.getText();
        return new Message(sender, text, receiver);

    }

    public static MessageDTO getDTO(Message message){
        String senderId=message.getSender().getId();
        String receiverId=message.getReceiver().getId();
        String txt=message.getText();
        return new MessageDTO(senderId, txt, receiverId);
    }

    public static UserDTO[] getDTO(User[] users){
        UserDTO[] frDTO=new UserDTO[users.length];
        for(int i=0;i<users.length;i++)
            frDTO[i]=getDTO(users[i]);
        return frDTO;
    }

    public static User[] getFromDTO(UserDTO[] users){
        User[] friends=new User[users.length];
        for(int i=0;i<users.length;i++){
            friends[i]=getFromDTO(users[i]);
        }
        return friends;
    }*/
}
