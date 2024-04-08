package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.AppException;
import org.example.ServiceInterface;
import org.example.model.Employee;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class LogInView {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passField;
    private ServiceInterface service; // this is from server
    Stage primaryStage;

    public void setService(Stage primaryStage ,ServiceInterface service) {
        this.service=service;
        this.primaryStage=primaryStage;
    }
    public void onLogInButtonClicked(ActionEvent actionEvent) throws IOException, AppException {
        String user =usernameField.getText();
        String pass=passField.getText();
        primaryStage.setTitle("FXML TableView Example");
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/agency-view.fxml"));
        Pane myPane = (Pane) loader.load();
        AgencyView ctrl=loader.getController();
        try {
            Optional<Employee> result=service.findUser(user,pass,ctrl);
            if(result.isEmpty())
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong","Username or password wrong...");
            else
            {
                ctrl.setService(primaryStage,service,result.get());
                Scene myScene = new Scene(myPane);
                primaryStage.setScene(myScene);
                   /* primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                //  System.out.println("Stage is closing");
                ctrl.close();
            }
        });*/
                primaryStage.show();
            }
        }
        catch (AppException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong",e.getMessage());
        }
 }


}
