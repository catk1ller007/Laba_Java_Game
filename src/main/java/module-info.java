module com.example.labaone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.labaone to javafx.fxml;
    exports com.example.labaone;
}