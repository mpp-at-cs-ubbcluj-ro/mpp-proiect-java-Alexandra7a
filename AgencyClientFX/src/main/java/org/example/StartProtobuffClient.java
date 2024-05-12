package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LogInView;
import org.example.protobuffprotocol.AgencyServiceProtoProxy;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffClient extends Application {
    private static int defaultChatPort=55556;
    private static String defaultServer="localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartProtobuffClient.class.getResourceAsStream("/agencyclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find agencyclient.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);

        ServiceInterface server=new AgencyServiceProtoProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(StartProtobuffClient.class.getClassLoader().getResource("log-in-view.fxml"));
        Parent root=loader.load();

        LogInView ctrl = loader.<LogInView>getController();
        ctrl.setService(primaryStage,server);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
