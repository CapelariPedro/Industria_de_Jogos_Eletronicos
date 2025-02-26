module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example to javafx.fxml;
    opens com.example.controllers to javafx.fxml;
    opens com.example.models to javafx.base; 
    exports com.example;
    exports com.example.controllers;
    exports com.example.models;
}
