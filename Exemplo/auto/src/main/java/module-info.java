module com.example.auto {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.auto to javafx.fxml;
    exports com.example.auto;
}
