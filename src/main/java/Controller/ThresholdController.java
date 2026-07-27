package Controller;

import Features.LowStock;
import Model.Products;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ThresholdController {

    private Products productToUpdate;

    @FXML
    private Label productNameLabel;

    @FXML
    private TextField ThresholdField;

    @FXML
    private Button okButton;


    public void setProduct(Products product) {
        productToUpdate = product;
        productNameLabel.setText(product.getCode() + " - " + product.getName());
        ThresholdField.setText(String.valueOf(product.getThreshold()));
    }

    @FXML
    private void onOkClick() {
        String text = ThresholdField.getText();
        if (text == null || text.isEmpty()) {
            showError("Please enter a threshold value.", "Missing Threshold");
            return;
        }

        int threshold;
        try {
            threshold = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showError("Enter a valid number", "Number Format Error");
            return;
        }

        String thresholdResult = LowStock.setLowStockThreshold(productToUpdate,threshold);

        switch (thresholdResult) {
            case "Success": {
                Stage thresholdWindow = (Stage) okButton.getScene().getWindow();
                thresholdWindow.close();
                break;
            }

            case "ThresholdError":
                showError("Threshold must be a number greater than zero.", "Invalid Threshold");
                break;

            case "LoadingError":
                showError("Could not load the current inventory. Please try again.", "Loading Error");
                break;

            case "TextFileError":
                showError("Could not save the new threshold. Please try again.", "Text File Error");
                break;

            default:
                showError("Could not set threshold: " + thresholdResult, "Unknown Error");
                break;
        }
    }

    private void showError(String message, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Set Threshold Failed");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
