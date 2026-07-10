package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.Parent;

public class MainController {
    @FXML
    private StackPane ContentPanel;

    @FXML
    private Button homeBtn;

    @FXML
    private Button inventoryBtn;

    @FXML
    private Button dealersBtn;

    @FXML
    private Button cartBtn;


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

    public void SetButtonColorOnActive(Button button){
        homeBtn.getStyleClass().remove("active-button");
        inventoryBtn.getStyleClass().remove("active-button");
        dealersBtn.getStyleClass().remove("active-button");
        cartBtn.getStyleClass().remove("active-button");

        button.getStyleClass().add("active-button");
    }

    @FXML
    private void onHomeButtonClick(){
        SetButtonColorOnActive(homeBtn);
        loadWindow("/FXML/Home.fxml");
    }

    @FXML
    private void onInventoryButtonClick(){
        SetButtonColorOnActive(inventoryBtn);
        loadWindow("/FXML/Inventory.fxml");
    }

    @FXML
    private void onDealerButtonClick(){
        SetButtonColorOnActive(dealersBtn);
        loadWindow("/FXML/Dealer.fxml");
    }

    @FXML
    private void onCartButtonClick(){
        SetButtonColorOnActive(cartBtn);
        loadWindow("/FXML/Cart.fxml");
    }
}