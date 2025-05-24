package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the HomePageAdmin.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePageAdmin.fxml"));
            Parent root = loader.load();
            
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