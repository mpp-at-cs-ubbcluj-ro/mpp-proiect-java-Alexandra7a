package org.example.rpcprotocol;

import org.example.AppException;
import org.example.ObserverInterface;
import org.example.ServiceInterface;
import org.example.model.Trip;
import org.example.model.dto.DTOUtils;
import org.example.model.dto.EmployeeDTO;
import org.example.model.Employee;
import org.example.model.dto.TripDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public class AgencyClientRpcWorker implements Runnable, ObserverInterface {
    private ServiceInterface server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AgencyClientRpcWorker(ServiceInterface server, Socket connection) {
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
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
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

    /*public void messageReceived(Message message) throws ChatException {
        MessageDTO mdto= DTOUtils.getDTO(message);
        Response resp=new Response.Builder().type(ResponseType.NEW_MESSAGE).data(mdto).build();
        System.out.println("Message received  "+message);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ChatException("Sending error: "+e);
        }
    }

    public void friendLoggedIn(User friend) throws ChatException {
        UserDTO udto= DTOUtils.getDTO(friend);
        Response resp=new Response.Builder().type(ResponseType.FRIEND_LOGGED_IN).data(udto).build();
        System.out.println("Friend logged in "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void friendLoggedOut(User friend) throws ChatException {
        UserDTO udto= DTOUtils.getDTO(friend);
        Response resp=new Response.Builder().type(ResponseType.FRIEND_LOGGED_OUT).data(udto).build();
        System.out.println("Friend logged out "+friend);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
      if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            EmployeeDTO udto=(EmployeeDTO)request.data();
            Employee user= DTOUtils.getFromDTO(udto);
            try {
                server.findUser(user.getUsername(), user.getPassword(),this);
                return okResponse;
            } catch (AppException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
         if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
           // LogoutRequest logReq=(LogoutRequest)request;
             EmployeeDTO udto=(EmployeeDTO)request.data();
            Employee employee= DTOUtils.getFromDTO(udto);
            try {
                server.logout(employee, this);
                connected=false;
                return okResponse;

            } catch (AppException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }


         ///am facut aici response pentru all trips
         if( request.type()== RequestType.FIND_ALL_TRIPS)
         {
             System.out.println("FindAllTripsRequest ...");
             try{
                 List<TripDTO> trips = (List<TripDTO>) server.getAllTrips();
                 connected=false;
                 return new Response.Builder().type(ResponseType.OK).data(trips).build();
             }catch (AppException e){
                 return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
             }
         }

         if(request.type()==RequestType.GET_RESERVATIONS)
         {
             System.out.println("FindReservationsAfterID ...");
             try{
                 long id= (long) request.data();
                 int no_reservation_at_hotel= server.getAllReservationsAt(id);
                 connected=false;
                 return new Response.Builder().type(ResponseType.OK).data(no_reservation_at_hotel).build();
             }catch (AppException e){
                 return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
             }
         }

         /*return new ErrorResponse("Invalid request");
         }
       if (request.type()== RequestType.SEND_MESSAGE){
            System.out.println("SendMessageRequest ...");
            MessageDTO mdto=(MessageDTO)request.data();
            Message message= DTOUtils.getFromDTO(mdto);
            try {
                server.sendMessage(message);
                return okResponse;
            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_LOGGED_FRIENDS){
            System.out.println("GetLoggedFriends Request ...");
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                User[] friends=server.getLoggedFriends(user);
                UserDTO[] frDTO= DTOUtils.getDTO(friends);
                return new Response.Builder().type(ResponseType.GET_LOGGED_FRIENDS).data(frDTO).build();
            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }*/
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    @Override
    public void userLoggedIn() {

    }

    @Override
    public void userLoggedOut() {

    }

    @Override
    public void reservationMade() {

    }
}
