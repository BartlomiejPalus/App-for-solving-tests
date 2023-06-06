module com.example.testy {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires java.desktop;

	opens com.example.testy to javafx.fxml;
    exports com.example.testy;
}