module com.example.testy {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.testy to javafx.fxml;
    exports com.example.testy;
}