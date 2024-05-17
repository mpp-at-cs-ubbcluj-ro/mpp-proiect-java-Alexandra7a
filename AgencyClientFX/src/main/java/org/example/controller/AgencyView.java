package org.example.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.AppException;
import org.example.ObserverInterface;
import org.example.ServiceInterface;
import org.example.model.Client;
import org.example.model.Employee;
import org.example.model.Reservation;
import org.example.model.Trip;
import org.example.model.dto.ClientDTO;
import org.example.model.dto.DTOUtils;
import org.example.model.dto.ReservationDTO;
import org.example.model.dto.TripDTO;

import javax.swing.text.Utilities;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

public class AgencyView implements ObserverInterface {

    @FXML
    TableView<Trip> tableTrip;
    @FXML
    TableColumn<Float,Trip> price;
    @FXML
    TableColumn<String,Trip> transportCompany;
    @FXML
    TableColumn<String,Trip> place;
    @FXML
    TableColumn<String, Trip>departure;
    @FXML
    TableColumn<Integer,Trip> noSeatsAvailable;

    @FXML
    TableView<Trip> tableTripFiltered;
    @FXML
    TableColumn<Float,Trip> priceFiltered;
    @FXML
    TableColumn<String,Trip> transportCompanyFiltered;
    @FXML
    TableColumn<String,Trip> placeFiltered;
    @FXML
    TableColumn<String, Trip> departureFiltered;
    @FXML
    TableColumn<Integer,Trip> noSeatsAvailableFiltered;

    @FXML
    DatePicker dateStartPicker;
    @FXML
    DatePicker dateEndPicker;
    @FXML
    ComboBox<Integer> hour2Combo;
    @FXML
    ComboBox<Integer> hour1Combo;
    @FXML
    TextField placeField;
    @FXML
    ListView<Client> clientList;

    @FXML
    TextField clientNameField;
    @FXML
    TextField phoneNumberField;
    @FXML
    TextField noSeatsField;


    private ServiceInterface service;
    private Stage primaryStage;
    private Employee responsibleEmployee;
    public void setService(Stage primaryStage,ServiceInterface service,Employee responsibleEmployee) {
        this.service=service;
        this.primaryStage=primaryStage;
        this.responsibleEmployee=responsibleEmployee;
        loadData();
        loadClientsData();
        primaryStage.setOnCloseRequest(event->{
            try
            {
                if(responsibleEmployee != null) {
                    service.logout(responsibleEmployee, this);
                }
                System.exit(0);
//                primaryStage.close();
            }
            catch (AppException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong",e.getMessage());
            }
        });
    }

    private void loadData() {
        try {
            tableTrip.getItems().clear();
            for (TripDTO trip:service.getAllTrips()) {
                int availableSeats=trip.getTotalSeats()-service.getAllReservationsAt(trip.getId());
                trip.setTotalSeats(availableSeats);
                Trip tripN= DTOUtils.getFromDTO(trip);
                tableTrip.getItems().add(tripN);
            }
        }
        catch (AppException e)
        {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong",e.getMessage());
        }
    }
    private void loadClientsData() {
        System.out.println("IN CLIENTI");
        try {
            clientList.getItems().clear();
            for (ClientDTO client : service.getAllClients()) {
                //todo transform clientDTO to client
                Client clientN=new Client(client.getName(),client.getBirthDate());
                clientN.setId(client.getId());
                clientList.getItems().add(clientN);
            }
        }catch (AppException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong",e.getMessage());
            }
    }



    private void loadFilteredData(String placeToVisit, LocalDateTime startTime, LocalDateTime endTime) {

        try {
            if (placeToVisit.isEmpty())
                throw new AppException("Empty field");
            tableTripFiltered.getItems().clear();
            for (TripDTO tripDTO : service.findAllTripPlaceTime(placeToVisit, startTime, endTime)) {
                int availableSeats = tripDTO.getTotalSeats() - service.getAllReservationsAt(tripDTO.getId());
                tripDTO.setTotalSeats(availableSeats);
                Trip trip=new Trip(tripDTO.getPlace(),tripDTO.getTransportCompanyName(),tripDTO.getDeparture(),tripDTO.getPrice(),tripDTO.getTotalSeats());
                tableTripFiltered.getItems().add(trip);
            }
        }catch(AppException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Something went wrong", e.getMessage());
            }
    }

   public void tripFilterButton(ActionEvent actionEvent) {
         try
        {
            String place=placeField.getText();
            LocalDate dateStart=dateStartPicker.getValue();
            LocalDate dateEnd=dateEndPicker.getValue();
            int hour2= hour1Combo.getValue();
            int hour1= hour2Combo.getValue();
            LocalDateTime startlocalDateTime=dateStart.atTime(hour1,0);
            LocalDateTime endlocalDateTime=dateEnd.atTime(hour2,0);
            loadFilteredData(place,startlocalDateTime,endlocalDateTime);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void execButton(ActionEvent actionEvent) {
    }

    public void cancelExecButton(ActionEvent actionEvent) {
    }

    public void addButton(ActionEvent actionEvent) {
    }

    public void deleteButton(ActionEvent actionEvent) {
    }

    public void updateButton(ActionEvent actionEvent) {
    }

    public void cancelButton(ActionEvent actionEvent) {
    }

    public void handleAddTaskRunner(ActionEvent actionEvent) {
    }

    public void handleExecuteOne(ActionEvent actionEvent) {
    }

    public void handleExecuteALL(ActionEvent actionEvent) {
    }
    @FXML
    public void initialize(){
       ArrayList<Integer> list=new ArrayList<>();
        for (int i=0;i<24;i++)
            list.add(i);
        hour1Combo.setItems(FXCollections.observableList(list));
        hour2Combo.setItems(FXCollections.observableList(list));
        System.out.println(hour1Combo.getValue());

    }

    public void onReserveButtonClicked(ActionEvent actionEvent) {
        //todo: check all fields to be filled
        String clientName=clientNameField.getText() ;
        String phoneNumber=phoneNumberField.getText();
        int noSeats=Integer.parseInt(noSeatsField.getText());
        Client client=clientList.getSelectionModel().getSelectedItem();
        var selection1= tableTripFiltered.getSelectionModel().getSelectedItem();
        var selection2= tableTrip.getSelectionModel().getSelectedItem();
        Trip trip;
        if (selection1!=null) {
            trip=selection1;
        } else trip=selection2;

//        System.out.println(clientName);
//        System.out.println(phoneNumber);
//        System.out.println(noSeats);
//        System.out.println(trip.getId());
//        System.out.println(client.toString());
        try {
            service.reserveTicket(clientName,phoneNumber,noSeats,trip,responsibleEmployee,client);
            MessageAlert.showMessage(primaryStage, Alert.AlertType.INFORMATION,"Info","Successful reservation...");
//            loadData();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            MessageAlert.showMessage(primaryStage, Alert.AlertType.ERROR,"Error","Could not save the reservation...");
        }
    }

    public void onRefreshButtonClicked(ActionEvent actionEvent) {
       // loadData();
    }



    @Override
    public void reservationMade() {

        Platform.runLater(this::loadData);
    }

    public void onLogOutButtonClicked(ActionEvent actionEvent) {
       try
       {
                service.logout(responsibleEmployee, this);
                primaryStage.close();
            }
            catch (AppException e)
            {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Something went wrong",e.getMessage());
            }
    }
}
