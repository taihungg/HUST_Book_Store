package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            // SỬA ĐỂ TẢI HomePageAdmin.fxml
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/admin/HomePageAdmin.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("HUST Book Store - Admin Panel"); // Có thể đặt lại tiêu đề
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            System.err.println("Lỗi khi tải HomePageAdmin.fxml:"); // In lỗi ra System.err
            e.printStackTrace();
        }
    }

}