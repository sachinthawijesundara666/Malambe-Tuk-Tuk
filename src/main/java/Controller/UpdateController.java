package Controller;

import Features.ProductManager;
import Model.Products;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateController {

    private Products productToUpdate;

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

    public void setProduct(Products product) {
        productToUpdate = product;
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        brandField.setText(product.getBrand());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        categoryField.setText(product.getCategory());
        dateField.setText(product.getDate());
        pictureField.setText(product.getPicture());
    }

    @FXML
    private void onOkClick() {
        String updateResult = ProductManager.update(
                productToUpdate.getCode(),
                nameField.getText(),
                brandField.getText(),
                priceField.getText(),
                quantityField.getText(),
                categoryField.getText(),
                dateField.getText(),
                pictureField.getText()
        );

        switch (updateResult) {
            case "Success": {
                Stage updateWindow = (Stage) okButton.getScene().getWindow();
                updateWindow.close();
                break;
            }

            case "CodeFormatError":
                showError("Code must be 4 characters, starting with 'P' followed by 3 digits (e.g. P011).", "Code Format Error");
                break;

            case "LoadingError":
                showError("Could not load the current inventory. Please try again.", "Loading Error");
                break;

            case "NumberFormatError":
                showError("Price and Quantity must be valid numbers.", "Number Format Error");
                break;

            case "DateFormatError":
                showError("Date must be in the format YYYY/MM/DD.", "Date Format Error");
                break;

            case "NotFound":
                showError("This product could not be found in the inventory.", "Item Not Found");
                break;

            case "TextFileError":
                showError("Could not save the changes. Please try again.", "Text File Error");
                break;

            default:
                showError("Could not update product: " + updateResult, "Unknown Error");
                break;
        }
    }

    private void showError(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update Product Failed");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
