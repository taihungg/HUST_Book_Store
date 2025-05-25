package controller;

import controller.admin.HomePageAdminController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	final String ADMIN_FXML_FILE_PATH = "/view/admin/HomePageAdmin.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ADMIN_FXML_FILE_PATH));
            HomePageAdminController controller = new HomePageAdminController();
            loader.setController(controller);

            // Load the HomePageAdmin.fxml
            FXMLLoader loader1 = new FXMLLoader(getClass().getClassLoader().getResource("view/admin/HomePageAdmin.fxml"));
            Parent root = loader1.load();
            
            // Set up the primary stage
            primaryStage.setTitle("HUST Book Store - Admin Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 