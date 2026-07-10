package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;

public class MainController {
    @FXML
    private StackPane ContentPanel;

    @FXML
    private void loadWindow(String FXML){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource(FXML));
            Parent root = fxmlLoader.load();
            ContentPanel.getChildren().setAll(root);

        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("Unable to load page");
            alert.setContentText("An unexpected error occurred.");

            alert.showAndWait();
        }

    }

    @FXML
    private void initialize(){
        loadWindow("/FXML/Home.fxml");
    }

    @FXML
    private void onHomeButtonClick(){
        loadWindow("/FXML/Home.fxml");
    }

    @FXML
    private void onInventoryButtonClick(){
        loadWindow("/FXML/Inventory.fxml");
    }

    @FXML
    private void onDealerButtonClick(){
        loadWindow("/FXML/Dealer.fxml");
    }

    @FXML
    private void onCartButtonClick(){
        loadWindow("/FXML/Cart.fxml");
    }
}