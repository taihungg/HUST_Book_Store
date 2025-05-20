module HUST_Book_Store {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports hust.soict.program.javafx;
    opens hust.soict.program.javafx to javafx.fxml;
    exports frontend;
}