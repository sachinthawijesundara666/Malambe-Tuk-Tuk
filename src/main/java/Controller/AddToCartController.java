package Controller;

import Features.CartManager;
import Model.Products;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddToCartController {

    private Products productAdd;

    @FXML
    private Label productNameLabel;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Button okButton;

    public void setProduct(Products product) {
        productAdd = product;
        productNameLabel.setText(product.getCode() + ": " + product.getName());
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.getQuantity(),1));
    }

    @FXML
    private void onOkClick (){
        int quantity = quantitySpinner.getValue();
        String cartStatus = CartManager.AddCart(productAdd, quantity);

        if (cartStatus.equals("AddedToCart")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart Message");
            alert.setHeaderText("Item Added to cart");
            alert.setContentText("Item " + productAdd.getCode() + " added to cart.");
            alert.showAndWait();
        }

        if (cartStatus.equals("Duplicate")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart Message");
            alert.setHeaderText("Duplicate Item");
            alert.setContentText("Item " + productAdd.getCode() + " already exists in cart.");
            alert.showAndWait();
        }

        if (cartStatus.equals("QuantityError")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart Message");
            alert.setHeaderText("Quantity Error");
            alert.setContentText("Quantity higher than available number of units.");
            alert.showAndWait();
        }

        Stage QuantityWindow = (Stage) okButton.getScene().getWindow();
        QuantityWindow.close();
    }

}
