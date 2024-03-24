package org.example;

        import javafx.application.Application;
        import javafx.event.EventHandler;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.layout.Pane;
        import javafx.stage.Stage;
        import javafx.stage.WindowEvent;
        import org.example.controller.AgencyView;
        import org.example.service.Service;

        import java.io.FileReader;
        import java.io.IOException;
        import java.util.Properties;

public class SortingFXMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXML TableView Example");
        FXMLLoader loader=new FXMLLoader(getClass().getResource("agency-view.fxml"));
        Pane myPane = (Pane) loader.load();
        AgencyView ctrl=loader.getController();

        ctrl.setService(getService());
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                //  System.out.println("Stage is closing");
                ctrl.close();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static Service getService(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("bd.config"));
            //System.setProperties(serverProps);

            System.out.println("Properties set. ");
            //System.getProperties().list(System.out);
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
            return null;
        }
        SortingTaskJdbcRepository repo=new SortingTaskJdbcRepository(serverProps);//Repository(new SortingTaskValidator());
        // SortingTaskRepository repo=new SortingTaskRepository(new SortingTaskValidator());
        ObservableTaskRunner runner=new ObservableTaskRunner(new TaskStack());
        TaskService service=new TaskService(repo,runner);
        return service;
    }
}
