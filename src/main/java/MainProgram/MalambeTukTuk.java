package MainProgram;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MalambeTukTuk extends Application{
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(MalambeTukTuk.class.getResource("/FXML/MalambeTukTuk.fxml"));
        Scene scene =  new Scene(fxmlloader.load(), 713, 437);
        stage.setTitle("Malambe Tuk Tuk Spare Parts Inventory Manager");
        stage.setScene(scene);
        stage.show();

    }

}