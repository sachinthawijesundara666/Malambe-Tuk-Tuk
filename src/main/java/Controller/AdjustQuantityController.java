package Controller;

import Features.CartManager;
import Model.CartItem;
import Model.Products;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class AdjustQuantityController {

    private CartItem cartItemToAdjust;
    private Products matchingProduct;
    private boolean quantityConfirmed = false;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Button okButton;

    public void setCartItem(CartItem cartItem, Products product) {
        cartItemToAdjust = cartItem;
        matchingProduct = product;
        itemNameLabel.setText(cartItem.getCode() + " - " + cartItem.getName());
        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.getQuantity(), cartItem.getQuantity())
        );
    }

    @FXML
    private void onOkClick() {
        int newQuantity = quantitySpinner.getValue();

        String result = CartManager.setQuantity(cartItemToAdjust, matchingProduct, newQuantity);

        if (result.equals("QuantitySet")) {
            quantityConfirmed = true;
            Stage adjustWindow = (Stage) okButton.getScene().getWindow();
            adjustWindow.close();
        } else if (result.equals("QuantityError")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adjust Quantity Failed");
            alert.setHeaderText("Invalid Quantity");
            alert.setContentText("Quantity must be greater than zero and not exceed available stock.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adjust Quantity Failed");
            alert.setHeaderText("Unknown Error");
            alert.setContentText("Could not adjust quantity: " + result);
            alert.showAndWait();
        }
    }

    public boolean wasConfirmed() {
        return quantityConfirmed;
    }
}
