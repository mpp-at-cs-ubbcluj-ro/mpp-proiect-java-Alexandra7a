package org.example.protobuffprotocol;

import org.example.AppException;
import org.example.ObserverInterface;
import org.example.ServiceInterface;
import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Trip;
import org.example.model.dto.*;
import org.example.rpcprotocol.Request;
import org.example.rpcprotocol.RequestType;
import org.example.rpcprotocol.Response;
import org.example.rpcprotocol.ResponseType;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AgencyClientProtoWorker implements Runnable, ObserverInterface {
    private ServiceInterface server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;
    public AgencyClientProtoWorker(ServiceInterface server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                //Object request=input.readObject();
                var request = AgencyProtocol.Request.parseDelimitedFrom(input);
                var response = handleRequest(request);

                //Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static AgencyProtocol.Response okResponse=ProtoUtils.createEmptyOkResponse();

    private AgencyProtocol.Response handleRequest(AgencyProtocol.Request request){
        AgencyProtocol.Response response=null;
        if (request.getType()== AgencyProtocol.Request.RequestType.LOGIN){
            System.out.println("Login request ..."+request.getType());
            EmployeeDTO udto=(EmployeeDTO)ProtoUtils.getEmployeeDTO(request);
            Employee user= DTOUtils.getFromDTO(udto);
            try {
                server.findUser(user.getUsername(), user.getPassword(),this);
                return okResponse;
            } catch (AppException e) {
                connected=false;
                return ProtoUtils.createErrorResponse(e.getMessage());
                //return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.getType()== AgencyProtocol.Request.RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            EmployeeDTO udto=ProtoUtils.getEmployeeDTO(request);
            Employee employee= DTOUtils.getFromDTO(udto);
            try {
                server.logout(employee, this);
                connected=false;
                return okResponse;

            } catch (AppException e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
                //return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if( request.getType()== AgencyProtocol.Request.RequestType.FIND_ALL_TRIPS)
        {
            System.out.println("FindAllTripsRequest ...");
            try{
                List<TripDTO> trips = (List<TripDTO>) server.getAllTrips();
                return ProtoUtils.createFindAllTripsResponse(trips);
                //return new Response.Builder().type(ResponseType.OK).data(trips).build();
            }catch (AppException e){
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType()==AgencyProtocol.Request.RequestType.GET_RESERVATIONS)
        {
            System.out.println("FindReservationsAfterID ...");
            try{
                long id= request.getId();
                int no_reservation_at_hotel= server.getAllReservationsAt(id);
                return ProtoUtils.createNumberReservationsResponse(no_reservation_at_hotel);
                //return new Response.Builder().type(ResponseType.OK).data(no_reservation_at_hotel).build();
            }catch (AppException e){
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType()==AgencyProtocol.Request.RequestType.FIND_ALL_CLIENTS)
        {
            System.out.println("Find All Clients ...");
            try{
                List<ClientDTO> clients = (List<ClientDTO>) server.getAllClients();
                return ProtoUtils.createFindAllClientsResponse(clients);
                //return new Response.Builder().type(ResponseType.OK).data(clients).build();
            }catch (AppException e){
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType()==AgencyProtocol.Request.RequestType.FILTER_TRIPS)
        {
            System.out.println("Filter Trips here...");
            try{
                TripFilterBy data= ProtoUtils.getTripFilterByDataFromRequest(request);

                String placeToVisit= data.getPlaceToVisit();;
                LocalDateTime startTime=data.getStartTime();
                LocalDateTime endTime=data.getEndTime();
                List< TripDTO> trips = (List<TripDTO>) server.getAllFilteredTripsPlaceTime(placeToVisit,startTime,endTime);
                return ProtoUtils.createFindAllFilteredTripsResponse(trips);
                // return new Response.Builder().type(ResponseType.OK).data(trips).build();
            }catch (AppException e){
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType()==AgencyProtocol.Request.RequestType.RESERVE_TICKET)
        {
            System.out.println("Reserve here...");
            try{
                ReservationDTO data= ProtoUtils.getReservationFromRequest(request);

                String clientName= data.getClientName();
                String phoneNumber= data.getPhoneNumber();
                int noSeats= data.getNoSeats();
                Trip trip=data.getTrip();
                Employee responsibleEmployee=data.getResponsibleEmployee();
                Client client=data.getClient();

                var var= server.saveReservation(clientName,phoneNumber,noSeats,trip,responsibleEmployee,client);
                return okResponse;
            }catch (AppException e){
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(AgencyProtocol.Response response) {
        System.out.println("sending response "+response);
        try {

            synchronized (output) {
                //output.writeObject(response);
                response.writeDelimitedTo(output);
                output.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void reservationUpdate() {
        AgencyProtocol.Response respone=ProtoUtils.createUpdateResponse();
        sendResponse(respone);
    }
}
