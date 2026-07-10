package MainProgram;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MalambeTukTuk extends Application{

    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(MalambeTukTuk.class.getResource("/FXML/Main.fxml"));
        Scene scene =  new Scene(fxmlloader.load(), 1280, 720);
        stage.setTitle("Malambe Tuk Tuk Spare Parts Inventory Manager");
        InputStream icon = MalambeTukTuk.class.getResourceAsStream("/Additional/images.png");
        if (icon!=null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.setScene(scene);
        stage.show();

    }

}