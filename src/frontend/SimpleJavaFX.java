package frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("🎉 Xin chào JavaFX!");
        Button button = new Button("Bấm để đổi nội dung");

        // Sự kiện khi nhấn nút
        button.setOnAction(e -> label.setText("✨ Bạn đã nhấn nút!"));

        VBox layout = new VBox(10); // VBox với khoảng cách 10px
        layout.getChildren().addAll(label, button);

        Scene scene = new Scene(layout, 300, 150);

        primaryStage.setTitle("Demo JavaFX cơ bản");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
