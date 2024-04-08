package org.example;

import org.example.rpcprotocol.AgencyServicesRpcProxy;
import org.example.ServiceInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.AgencyView;
import org.example.controller.LogInView;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/agencyclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find agencyclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        //get the server's service implementation
        ServiceInterface server = new AgencyServicesRpcProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("log-in-view.fxml"));
        Parent root=loader.load();

        LogInView ctrl = loader.<LogInView>getController();
        ctrl.setService(primaryStage,server);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}


