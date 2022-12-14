module com.example.laba6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.laba6 to javafx.fxml;
    exports com.example.laba6;
}