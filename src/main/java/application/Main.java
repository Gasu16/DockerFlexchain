package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Main extends Application {

    private Stage primaryStage;
    private TabPane rootLayout;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FlexChain");
        this.primaryStage.setOnCloseRequest(e -> System.exit(0));
        initRootLayout();
    }

    private void initRootLayout() {
        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FlexChainInterface.fxml"));
            rootLayout =(TabPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
