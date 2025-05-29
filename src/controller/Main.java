package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.manager.user.UserManager;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Book;
import model.user.User;

public class Main extends Application {

    public static AppServiceManager appServiceManager = new AppServiceManager();

    public static User currentUser =appServiceManager.getCurrentUser() ;

    public static UserManager userManager = appServiceManager.getUserManager();
    public static User currentUser;

    public static void main(String[] args) {
                launch(args);
            }
        
            @Override
            public void start(Stage primaryStage) {
                try{
                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/customer/HomePage.fxml"));
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
}