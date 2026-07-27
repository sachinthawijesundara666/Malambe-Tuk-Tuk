package Controller;

import Model.Products;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FilterController {

    private boolean filtersConfirmed = false;

    @FXML
    private ComboBox<String> categoryCombo;

    @FXML
    private CheckBox lowStockCheck;

    @FXML
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

    @FXML
    private Button okButton;

    public void setFilter(String category, boolean lowStock, Double minPrice, Double maxPrice) {
        categoryCombo.setValue(category);
        lowStockCheck.setSelected(lowStock);
        if (minPrice == null) {
            minPriceField.setText("");
        } else minPriceField.setText(String.valueOf(minPrice));

        if (maxPrice == null) {
            maxPriceField.setText("");
        } else maxPriceField.setText(String.valueOf(maxPrice));

    }


    public void setAvailableCategories(Products[] products) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("All");

        for (Products product : products) {
            if (!alreadyInList(categories, product.getCategory())) {
                categories.add(product.getCategory());
            }
        }

        categoryCombo.getItems().setAll(categories);
        categoryCombo.setValue("All");
    }


    private boolean alreadyInList(ArrayList<String> list, String value) {
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }


    @FXML
    private void onOkClick(){
        String minText = minPriceField.getText();
        String maxText = maxPriceField.getText();

        Double minValue = null;
        Double maxValue = null;

        if (minText != null && !minText.isEmpty()) {
            try {
                minValue = Double.parseDouble(minText);
            } catch (NumberFormatException e) {
                showError("Minimum price must be a valid number.");
                return;
            }
        }

        if (maxText != null && !maxText.isEmpty()) {
            try {
                maxValue = Double.parseDouble(maxText);
            } catch (NumberFormatException e) {
                showError("Maximum price must be a valid number.");
                return;
            }
        }

        if (minValue != null && maxValue != null && minValue > maxValue) {
            showError("Minimum price cannot be greater than maximum price.");
            return;
        }


        filtersConfirmed = true;
        Stage filterWindow = (Stage) okButton.getScene().getWindow();
        filterWindow.close();
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Filter Failed");
        alert.setHeaderText("Invalid Filter");
        alert.setContentText(message);
        alert.showAndWait();
    }


    public boolean wasConfirmed() {
        return filtersConfirmed;
    }


    public String getSelectedCategory() {
        return categoryCombo.getValue();
    }


    public boolean isLowStockOnly() {
        return lowStockCheck.isSelected();
    }


    public Double getMinPrice() {
        String text = minPriceField.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }
        else {
            return  Double.parseDouble(text);
        }
    }


    public Double getMaxPrice() {
        String text = maxPriceField.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }
        else {
            return  Double.parseDouble(text);
        }
    }
}
