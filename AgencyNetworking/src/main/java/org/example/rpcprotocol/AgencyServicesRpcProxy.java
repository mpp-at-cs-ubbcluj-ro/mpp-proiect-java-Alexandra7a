package org.example.rpcprotocol;

import org.example.model.dto.*;

import org.example.AppException;
import org.example.ObserverInterface;
import org.example.ServiceInterface;
import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Reservation;
import org.example.model.Trip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class AgencyServicesRpcProxy implements ServiceInterface {
    private String host;
    private int port;

    private ObserverInterface client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AgencyServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public Iterable<TripDTO> findAllTripPlaceTime(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime) throws AppException {

        Request req = new Request.Builder().type(RequestType.FILTER_TRIPS).data(new TripFilterBy(placeToVisit, startTime, endTime)).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.OK){
            return (Iterable<TripDTO>) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        return null;
    }

    @Override
    public int getAllReservationsAt(Long id) throws AppException {

        Request req = new Request.Builder().type(RequestType.GET_RESERVATIONS).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.OK){
            return (int) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        return 0;
    }

    @Override
    public Iterable<TripDTO> getAllTrips() throws AppException {
        Request req = new Request.Builder().type(RequestType.FIND_ALL_TRIPS).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.OK){
            return (Iterable<TripDTO>) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        return null;
    }


    /**for login*/
    @Override
    public Optional<Employee> findUser(String user, String pass, ObserverInterface client) throws AppException {

        //TODo: sa il trimiti la server sa indeplineasca cererea
        initializeConnection();
        EmployeeDTO employeeDTO = new EmployeeDTO(user, pass);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(employeeDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return Optional.of(DTOUtils.getFromDTO(employeeDTO));
        }
        if (response.type().equals(ResponseType.ERROR)) {
            closeConnection();
            String err = response.data().toString();
            throw new AppException(err);
        }
        return Optional.empty();
    }


/*    public void login(Employee employee, ObserverInterface client) throws AppException {
        initializeConnection();
        EmployeeDTO edto = DTOUtils.getDTO(employee);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(edto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new AppException(err);
        }
    }*/


    public void logout(Employee employee, ObserverInterface client) throws AppException {
        EmployeeDTO edto = DTOUtils.getDTO(employee);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(edto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public Iterable<ClientDTO> getAllClients() throws AppException {
        Request req = new Request.Builder().type(RequestType.FIND_ALL_CLIENTS).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.OK){
            return (Iterable<ClientDTO>) response.data();
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new AppException(err);
        }
        return null;
    }

    @Override
    public Optional<ReservationDTO> reserveTicket(String clientName, String phoneNumber, int noSeats, Trip trip, Employee responsibleEmployee, Client client) throws AppException {

        ReservationDTO reservationDTO=new ReservationDTO(clientName,phoneNumber,noSeats,trip,responsibleEmployee,client);
        Request req = new Request.Builder().type(RequestType.RESERVE_TICKET).data(reservationDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.OK){
            return Optional.of(reservationDTO);
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
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

    private void sendRequest(Request request) throws AppException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AppException("Error sending object " + e);
        }
    }

    private Response readResponse() throws AppException {
        Response response = null;
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
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) throws IOException {

        if (response.type() == ResponseType.UPDATE) {
            //TODO: ce tip
            System.out.println("Ceva acolo ceva");
            client.reservationMade();
        }
       /* if (response.type()== ResponseType.FRIEND_LOGGED_IN){

            User friend= DTOUtils.getFromDTO((UserDTO) response.data());
            System.out.println("Friend logged in "+friend);
            try {
                client.friendLoggedIn(friend);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
        if (response.type()== ResponseType.FRIEND_LOGGED_OUT){
            User friend= DTOUtils.getFromDTO((UserDTO)response.data());
            System.out.println("Friend logged out "+friend);
            try {
                client.friendLoggedOut(friend);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }

        if (response.type()== ResponseType.NEW_MESSAGE){
            Message message= DTOUtils.getFromDTO((MessageDTO)response.data());
            try {
                client.messageReceived(message);
            } catch (AppException e) {
                e.printStackTrace();
            }
        }*/
    }


    /**
     * to treat the coming responses which were not requested(eg: a friend logged out, the list modified )
     */
    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE || response.type() == ResponseType.FRIEND_LOGGED_OUT || response.type() == ResponseType.FRIEND_LOGGED_IN || response.type() == ResponseType.NEW_MESSAGE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
