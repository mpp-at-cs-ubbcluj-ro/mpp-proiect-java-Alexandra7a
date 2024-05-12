package org.example.protobuffprotocol;

import org.example.AppException;
import org.example.ObserverInterface;
import org.example.ServiceInterface;
import org.example.model.Client;
import org.example.model.Trip;
import org.example.model.dto.*;
import org.example.model.Employee;
import org.example.rpcprotocol.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgencyServiceProtoProxy implements  ServiceInterface {
    private  String host; // modified here to final!!
    private  int port;

    private ObserverInterface client;
    //private ObjectInputStream input;

    //private ObjectOutputStream output;
    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<AgencyProtocol.Response> qresponses;
    private volatile boolean finished;

    public AgencyServiceProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<AgencyProtocol.Response>();
    }

    @Override
    public Iterable<TripDTO> getAllFilteredTripsPlaceTime(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime) throws AppException {

        // Request req = new Request.Builder().type(RequestType.FILTER_TRIPS).data(new TripFilterBy(placeToVisit, startTime, endTime)).build();
        AgencyProtocol.Request req =ProtoUtils.createGetTripsFilteredRequest(new TripFilterBy(placeToVisit, startTime, endTime));
        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        if(response.getType()==AgencyProtocol.Response.ReponseType.OK){
            return ProtoUtils.getTripsFromResponse(response);
        }
        if (response.getType() ==AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return null;
    }

    @Override
    public int getAllReservationsAt(Long id) throws AppException {

        //Request req = new Request.Builder().type(RequestType.GET_RESERVATIONS).data(id).build();
        AgencyProtocol.Request req=ProtoUtils.createGetAllReservationsNumberRequest(id);
        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        if(response.getType()==AgencyProtocol.Response.ReponseType.OK){
            return ProtoUtils.getReservationNumberFromResponse(response);
        }
        if (response.getType() == AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return 0;
    }

    @Override
    public Iterable<TripDTO> getAllTrips() throws AppException {
        //Request req = new Request.Builder().type(RequestType.FIND_ALL_TRIPS).build();
        AgencyProtocol.Request req= ProtoUtils.createGetAllTripsRequest();
        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        if(response.getType()==AgencyProtocol.Response.ReponseType.OK){
            return ProtoUtils.getTripsFromResponse(response);
        }
        if (response.getType() == AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return null;
    }


    /**for login*/
    @Override
    public Optional<Employee> findUser(String user, String pass, ObserverInterface client) throws AppException {

        //  il trimiti la server sa indeplineasca cererea
        initializeConnection();
        EmployeeDTO employeeDTO = new EmployeeDTO(user, pass);
        //Request req = new Request.Builder().type(RequestType.LOGIN).data(employeeDTO).build();
        var request= ProtoUtils.createLoginRequest(employeeDTO);
        sendRequest(request);
        //Response response = readResponse();
        AgencyProtocol.Response response = readResponse();
        if (response.getType() ==AgencyProtocol.Response.ReponseType.OK) {
            this.client = client;
            return Optional.of(DTOUtils.getFromDTO(employeeDTO));
        }
        if (response.getType().equals(AgencyProtocol.Response.ReponseType.ERROR)) {
            closeConnection();
            String err = response.getError();
            throw new AppException(err);
        }
        return Optional.empty();
    }

    public void logout(Employee employee, ObserverInterface client) throws AppException {
        EmployeeDTO edto = DTOUtils.getDTO(employee);
        //Request req = new Request.Builder().type(RequestType.LOGOUT).data(edto).build();
        AgencyProtocol.Request req=ProtoUtils.createLogoutRequest(edto);
        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        closeConnection();
        if (response.getType() == AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
    }

    @Override
    public Iterable<ClientDTO> getAllClients() throws AppException {
        //Request req = new Request.Builder().type(RequestType.FIND_ALL_CLIENTS).build();
        AgencyProtocol.Request req=ProtoUtils.createFindAllClientsRequest();
        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        if(response.getType()==AgencyProtocol.Response.ReponseType.OK){
            return ProtoUtils.getClientsFromResponse(response);
        }
        if (response.getType() == AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return null;
    }

    @Override
    public Optional<ReservationDTO> saveReservation(String clientName, String phoneNumber, int noSeats, Trip trip, Employee responsibleEmployee, Client client) throws AppException {

        ReservationDTO reservationDTO=new ReservationDTO(clientName,phoneNumber,noSeats,trip,responsibleEmployee,client);
        //Request req = new Request.Builder().type(RequestType.RESERVE_TICKET).data(reservationDTO).build();
        AgencyProtocol.Request req=ProtoUtils.createReservationTicketRequest(reservationDTO);

        sendRequest(req);
        AgencyProtocol.Response response = readResponse();
        if(response.getType()==AgencyProtocol.Response.ReponseType.OK){
            return Optional.of(reservationDTO);
        }
        if (response.getType() == AgencyProtocol.Response.ReponseType.ERROR) {
            String err = response.getError();
            throw new AppException(err);
        }
        return Optional.empty();
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(AgencyProtocol.Request request) throws AppException {
        try {
            //output.writeObject(request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }
    }

    private AgencyProtocol.Response readResponse() throws AppException {
        AgencyProtocol.Response response = null;
        try {
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
//            output.flush();
            input = connection.getInputStream();
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new AgencyServiceProtoProxy.ReaderThread());
        tw.start();
    }

    private void handleUpdate(AgencyProtocol.Response response) throws IOException {

        if (response.getType() == AgencyProtocol.Response.ReponseType.UPDATE) {
            System.out.println("Update handleUpdate Proxy");
            client.reservationMade();
        }
    }


    /**
     * to treat the coming responses which were not requested(eg: a friend logged out, the list modified )
     */
    private boolean isUpdate(AgencyProtocol.Response response) {
        return response.getType() == AgencyProtocol.Response.ReponseType.UPDATE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    //Object response = input.readObject();
                    AgencyProtocol.Response response = AgencyProtocol.Response.parseDelimitedFrom(input);

                    System.out.println("response received " + response);
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);

                }
            }
        }
    }


}
