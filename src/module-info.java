module HUST_Book_Store {
	requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens frontend to javafx.fxml;
    opens frontend.view.admin to javafx.fxml;
    exports frontend;
    exports frontend.view.admin;
}
