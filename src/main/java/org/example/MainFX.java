package org.example;

        import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;
        import org.example.controller.LogInView;
        import org.example.repository.*;
        import org.example.repository.interfaces.ClientRepository;
        import org.example.repository.interfaces.EmployeeRepository;
        import org.example.repository.interfaces.ReservationRepository;
        import org.example.repository.interfaces.TripRepository;
        import org.example.service.Service;

        import java.io.FileReader;
        import java.io.IOException;
        import java.util.Properties;

public class MainFX extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXML TableView Example");
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/log-in-view.fxml"));
        Pane myPane = (Pane) loader.load();
        LogInView ctrl=loader.getController();

        ctrl.setService(primaryStage,getService());
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

    public static void main(String[] args) {
        launch(args);
    }

    static Service getService(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("db.config"));
            //System.setProperties(serverProps);

            System.out.println("Properties set. ");
            //System.getProperties().list(System.out);
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
            return null;
        }
        TripRepository tripRepository=new TripDBRepository(serverProps);
        ReservationRepository reservationRepository=new ReservationDBRepository(serverProps);
        EmployeeRepository employeeRepository=new EmployeeDBRepository(serverProps);
        ClientRepository clientRepository=new ClientDBRepository(serverProps);
        return new Service(tripRepository,reservationRepository,employeeRepository,clientRepository);
    }
}
