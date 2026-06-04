module restaurant.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens service to javafx.fxml;
    opens model to javafx.fxml;

    exports model;
    exports service;
    exports repository;
}