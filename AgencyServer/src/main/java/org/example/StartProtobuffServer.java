package org.example;
import org.example.ServiceInterface;
import org.example.repository.ClientDBRepository;
import org.example.repository.EmployeeDBRepository;
import org.example.repository.ReservationDBRepository;
import org.example.repository.TripDBRepository;
import org.example.repository.interfaces.ClientRepository;
import org.example.repository.interfaces.EmployeeRepository;
import org.example.repository.interfaces.ReservationRepository;
import org.example.repository.interfaces.TripRepository;
import org.example.server.ServicesImplementation;
import org.example.utils.AbstractServer;
import org.example.utils.AgencyProtoConcurrentServer;
import org.example.utils.AgencyRpcConcurrentServer;
import org.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;


public class StartProtobuffServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {


        Properties serverProps=new Properties();
        try {
            serverProps.load(StartProtobuffServer.class.getResourceAsStream("/chatserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        TripRepository tripRepository=new TripDBRepository(serverProps);
        ReservationRepository reservationRepository=new ReservationDBRepository(serverProps);
        EmployeeRepository employeeRepository=new EmployeeDBRepository(serverProps);
        ClientRepository clientRepository=new ClientDBRepository(serverProps);
        ServiceInterface chatServerImpl=new ServicesImplementation(tripRepository,reservationRepository,employeeRepository,clientRepository);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);


        AbstractServer server = new AgencyProtoConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.out.println("Ceva");
            System.err.println("Error starting the server" + e.getMessage());
        }

    }
}
