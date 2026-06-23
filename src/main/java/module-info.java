module org.example.malambe_tuk_tuk {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.malambe_tuk_tuk to javafx.fxml;
    exports org.example.malambe_tuk_tuk;
}