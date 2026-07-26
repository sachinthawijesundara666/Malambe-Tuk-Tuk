package Controller;

import Features.ProductManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddController {

    @FXML
    private TextField codeField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField pictureField;

    @FXML
    private Button okButton;


    @FXML
    private void onOkClick() {
        String result = ProductManager.addProduct(
                codeField.getText(),
                nameField.getText(),
                brandField.getText(),
                priceField.getText(),
                quantityField.getText(),
                categoryField.getText(),
                dateField.getText(),
                pictureField.getText()
        );
        switch (result) {
            case "Success": {
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
                break;
            }

            case "NoCode":
                showError("Please enter a product code.", "No Product Code");
                break;

            case "CodeFormatError":
                showError("Code must be 4 characters, starting with 'P' followed by 3 digits (e.g. P011).", "Code Format Error");
                break;

            case "NoName":
                showError("Please enter a product name.", "Name Field Empty");
                break;

            case "NumberFormatError":
                showError("Price and Quantity must be valid numbers.", "Number Format Error");
                break;

            case "DateFormatError":
                showError("Date must be in the format YYYY/MM/DD.", "Date Format Error");
                break;

            case "Duplicate":
                showError("A product with this code already exists.", "Duplicate Item");
                break;

            case "TextFileError":
                showError("Could not save the product. Please try again.", "Text File Error");
                break;

            default:
                showError("Could not add product: " + result, "Unknown Error");
                break;
        }
    }

    private void showError(String message, String HeaderStr) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add Product Failed");
        alert.setHeaderText(HeaderStr);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}
