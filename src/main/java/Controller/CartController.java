package Controller;

import Features.CartManager;
import Features.ProductManager;
import Model.CartItem;
import Model.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CartController {

    @FXML
    TableView<CartItem> cartTable;

    @FXML
    TableColumn<CartItem, String> codeColumn;

    @FXML
    TableColumn<CartItem, String> nameColumn;

    @FXML
    TableColumn<CartItem, String> brandColumn;

    @FXML
    TableColumn<CartItem, Double> priceColumn;

    @FXML
    TableColumn<CartItem, Integer> quantityColumn;

    @FXML
    TableColumn<CartItem, String> categoryColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadCart();

        String totalDisplay = CartManager.Total();
        totalLabel.setText(totalDisplay);
    }

    private void loadCart(){
        CartItem[] cartItems = CartManager.getBasket().toArray(new CartItem[0]);
        cartTable.setItems(ConvObservableList(cartItems));
    }

    private ObservableList<CartItem> ConvObservableList(CartItem[] cartItems) {
        ObservableList<CartItem> cartList = FXCollections.observableArrayList();
        if (cartItems != null) {
            cartList.addAll(cartItems);
        }
        return cartList;
    }


    @FXML
    private void onRemoveButtonClick() {
        CartItem cartItem = cartTable.getSelectionModel().getSelectedItem();
        CartManager.RemoveCart(cartItem);
        loadCart();
        totalLabel.setText(CartManager.Total());
    }

    @FXML
    private void onAdjustQuantityButtonClick() {
        CartItem cartItem = cartTable.getSelectionModel().getSelectedItem();

        if (cartItem == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Adjust Quantity");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select an item before adjusting quantity.");
            alert.showAndWait();
            return;
        }

        Products[] products = ProductManager.loadFromNewFile();
        Products matchingProduct = null;

        if (products != null) {
            for (Products p : products) {
                if (p.getCode().equals(cartItem.getCode())) {
                    matchingProduct = p;
                    break;
                }
            }
        }

        if (matchingProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adjust Quantity");
            alert.setHeaderText("Product Not Found");
            alert.setContentText("This item not found in the inventory. ");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CartController.class.getResource("/FXML/AdjustQuantity.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AdjustQuantityController adjustQuantityController = fxmlLoader.getController();
            adjustQuantityController.setCartItem(cartItem, matchingProduct);

            Stage stage = new Stage();
            stage.setTitle("Adjust Quantity");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            if (adjustQuantityController.wasConfirmed()) {
                loadCart();
                totalLabel.setText(CartManager.Total());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Adjust Quantity Controller Error");
            alert.setHeaderText("Adjust Quantity Window Error");
            alert.setContentText("Error while opening Adjust Quantity window.");
            alert.showAndWait();
        }
    }
}
