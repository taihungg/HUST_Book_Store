package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Book;




public class Main extends Application {

    private static AppServiceManager appServiceManager;

    public static void main(String[] args) {
        addSample();
                launch(args);
            }
        
            @Override
            public void start(Stage primaryStage) {
                try{
                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/admin/HomePageAdmin.fxml"));
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        public static void addSample(){
        appServiceManager = AppServiceManager.getInstance();
        

        // --- 2. Khởi tạo đối tượng Stationery ---
        // Constructor Stationery bạn cung cấp:
        // Stationery(String id, String title, String description, String galleryURL, double sellingPrice,
        //            double purchasePrice, double averageRating, int numberOfReviews, String status,
        //            String brand, String type)
        Stationery sampleStationery = new Stationery(
            "S001",                                     // id
            "Hộp Bút Chì Màu Classmate 24 Màu",          // title
            "Bút chì màu thân gỗ, màu sắc tươi sáng, an toàn cho trẻ.", // description
            "http://example.com/images/classmate_pencils.jpg", // galleryURL
            65000.0,                                    // sellingPrice
            40000.0,                                    // purchasePrice
            4.5,                                        // averageRating
            120,                                        // numberOfReviews
            "Còn hàng",                                 // status
            "Classmate (Thiên Long)",                   // brand
            "Bút chì màu"                               // type
        );
        System.out.println("Đã tạo Stationery: " + sampleStationery.getTitle());

        // --- 3. Khởi tạo đối tượng Toy ---
        // Constructor Toy bạn cung cấp:
        // Toy(String id, String title, String description, String galleryURL, double sellingPrice,
        //     double purchasePrice, double averageRating, int numberOfReviews, String status,
        //     String brand, int suitableAge)
        Toy sampleToy = new Toy(
            "T001",                                     // id
            "Xe Điều Khiển Địa Hình Rock Crawler",        // title
            "Xe điều khiển từ xa mạnh mẽ, vượt mọi địa hình.", // description
            "http://example.com/images/rock_crawler.jpg",// galleryURL
            450000.0,                                   // sellingPrice
            300000.0,                                   // purchasePrice
            4.7,                                        // averageRating
            90,                                         // numberOfReviews
            "Còn hàng",                                 // status
            "RC Toys",                                  // brand
            8                                           // suitableAge
        );
        System.out.println("Đã tạo Toy: " + sampleToy.getTitle());

        appServiceManager.getProductManager().addProduct(sampleStationery, 10,appServiceManager.getCurrentUser());
        appServiceManager.getProductManager().addProduct(sampleToy, 10,appServiceManager.getCurrentUser());
        System.out.println(appServiceManager.getCurrentUser());
    }


}