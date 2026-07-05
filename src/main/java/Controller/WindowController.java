package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowController {

    @FXML
    private Label help;

    @FXML
    protected void onHelpClick(){help.setText("Welcome!");
    }

    //Opening a new Window for add section
    @FXML
    protected void onAddButtomClick() throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(WindowController.class.getResource("/FXML/AddWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600,400);

        Stage stage = new Stage();
        stage.setTitle("Add Items");
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    protected void onEditButtonClick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(WindowController.class.getResource("/FXML/EditWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600,400);

        Stage stage = new Stage();
        stage.setTitle("Update Items");
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    protected void onDeleteButtonClick() throws IOException{
    FXMLLoader fxmlLoader = new FXMLLoader(WindowController.class.getResource("/FXML/DeleteWindow.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600,400);

    Stage stage = new Stage();
    stage.setTitle("Delete Items");
    stage.setScene(scene);
    stage.show();
        }
    }



