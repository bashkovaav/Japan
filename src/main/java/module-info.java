module com.example.japan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.japan to javafx.fxml;
    exports com.example.japan;
}