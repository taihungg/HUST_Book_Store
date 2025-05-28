package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.manager.AppServiceManager;

public class Main extends Application {

    private static AppServiceManager appServiceManager = new AppServiceManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tạo FXMLLoader để tải HomePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/HomePage.fxml"));
            
            // Sử dụng setControllerFactory để tiêm AppServiceManager vào controller
            loader.setControllerFactory(controllerClass -> {
                try {
                    // Tạo instance của controller
                    Object controller = controllerClass.getDeclaredConstructor().newInstance();
                    // Gọi setAppServiceManager nếu controller có phương thức này
                    try {
                        controllerClass.getMethod("setAppServiceManager", AppServiceManager.class)
                            .invoke(controller, appServiceManager);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Controller " + controllerClass.getName() + " không có phương thức setAppServiceManager. Bỏ qua.");
                    }
                    return controller;
                } catch (Exception e) {
                    throw new RuntimeException("Không thể khởi tạo controller: " + controllerClass.getName(), e);
                }
            });

            // Tải FXML
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("HustBookStore");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Lỗi khởi động ứng dụng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static AppServiceManager getAppServiceManager() {
        return appServiceManager;
    }
}