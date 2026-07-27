package Controller;

import Features.LowStock;
import Features.ProductManager;
import Model.Products;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class InventoryController {

    @FXML
    private TableView<Products> productsTable;

    @FXML
    private TableColumn<Products, String> codeColumn;

    @FXML
    private TableColumn<Products, String> nameColumn;

    @FXML
    private TableColumn<Products, String> brandColumn;

    @FXML
    private TableColumn<Products, Double> priceColumn;

    @FXML
    private TableColumn<Products, Integer> quantityColumn;

    @FXML
    private TableColumn<Products, String> categoryColumn;

    @FXML
    private TableColumn<Products, String> dateColumn;

    @FXML
    private TableColumn<Products, String> stockColumn;

    @FXML
    private void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        stockColumn.setCellValueFactory(cellData -> {
            Products product = cellData.getValue();
            String Stock;
            if (LowStock.isLowStock(product)) {
                Stock = "Low Stock";
            }else {
                Stock = "In Stock";
            }
            return new SimpleStringProperty(Stock);
        });

        loadProducts();
    }

    private void loadProducts() {
        Products[] productlist = ProductManager.loadFromNewFile();
        productsTable.setItems(toObservableList(productlist));
    }

    private ObservableList<Products> toObservableList(Products[] products) {
        ObservableList<Products> productlist = FXCollections.observableArrayList();
        if (products != null) {
            productlist.addAll(products);
        }
        return productlist;
    }

    @FXML
    private void onDeleteButtonClick () {
        Products product = productsTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Message");
            alert.setHeaderText("No product selected");
            alert.setContentText("Please select a product before proceeding.");
            alert.showAndWait();
        }else {
            Products[] legacyProducts = ProductManager.load("inventory_legacy.txt");

            if (legacyProducts == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Null Pointer error");
                alert.setHeaderText("Error Loading Products");
                alert.setContentText("Error occurred while loading from text file.");
                alert.showAndWait();
                return;
            }

            for (Products p : legacyProducts) {
                if (p.getCode().equals(product.getCode())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Non Deletable Item");
                    alert.setHeaderText("Legacy Product");
                    alert.setContentText("Product selected is a legacy product, cannot be deleted.");
                    alert.showAndWait();
                    return;
                }
            }
            String deletedStatus = ProductManager.delete(product.getCode());

            if (deletedStatus.equals("Success")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Deleted");
                alert.setHeaderText("Item Deleted");
                alert.setContentText("Item " + product.getCode() + " deleted.");
                alert.showAndWait();
            }
            if (deletedStatus.equals("NoCode")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Code Not Found");
                alert.setHeaderText("Code Not Found");
                alert.setContentText("Item " + product.getCode() + "  code not found.");
                alert.showAndWait();
            }
            if (deletedStatus.equals("LoadingError")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading Error");
                alert.setHeaderText("Loading Error");
                alert.setContentText("Loading error met.");
                alert.showAndWait();
            }
            if (deletedStatus.equals("TextFileError")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Text File Error");
                alert.setHeaderText("Text File Loading Error");
                alert.setContentText("Error while opening text file.");
                alert.showAndWait();
            }
            if (deletedStatus.equals("NotFound")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Code Not Found");
                alert.setHeaderText("Code Not In Text File");
                alert.setContentText("Product with selected code could not be found in text file.");
                alert.showAndWait();
            }
        }Products[] refreshedProducts = ProductManager.loadFromNewFile();
        productsTable.setItems(toObservableList(refreshedProducts));
    }

    @FXML
    private void onAddButtonClick(){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(InventoryController.class.getResource("/FXML/Add.fxml"));
            Scene scene = new Scene(fxmlloader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            Products[] refreshedProducts = ProductManager.loadFromNewFile();
            productsTable.setItems(toObservableList(refreshedProducts));
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Controller Error");
            alert.setHeaderText("Add Window Error");
            alert.setContentText("Error while opening add product window.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onUpdateButtonClick() {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(InventoryController.class.getResource("/FXML/Update.fxml"));
            Scene scene = new Scene(fxmlloader.load());
            Stage stage = new Stage();
            stage.setTitle("Update Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            Products product = productsTable.getSelectionModel().getSelectedItem();
            if (product == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Product Selected");
                alert.setHeaderText("No Product Selected");
                alert.setContentText("No product selected to update.");
                alert.showAndWait();
                return;
            }

            Products[] legacyProducts = ProductManager.load("inventory_legacy.txt");

            if (legacyProducts == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Null Pointer error");
                alert.setHeaderText("Error Loading Products");
                alert.setContentText("Error occurred while loading from text file.");
                alert.showAndWait();
                return;
            }

            for (Products p : legacyProducts) {
                if (p.getCode().equals(product.getCode())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Non Updatable Item");
                    alert.setHeaderText("Legacy Product");
                    alert.setContentText("Product selected is a legacy product, cannot be updated.");
                    alert.showAndWait();
                    return;
                }
            }

            UpdateController updateController = fxmlloader.getController();
            updateController.setProduct(product);
            stage.showAndWait();

            Products[] refreshedProducts = ProductManager.loadFromNewFile();
            productsTable.setItems(toObservableList(refreshedProducts));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Controller Error");
            alert.setHeaderText("Update Window Error");
            alert.setContentText("Error while opening Update product window.");
            alert.showAndWait();
        }


    }

    @FXML
    private void onThresholdButtonClick() {
        Products product = productsTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Product Selected");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("No product selected to set threshold.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlloader = new FXMLLoader(InventoryController.class.getResource("/FXML/Threshold.fxml"));
            Scene scene = new Scene(fxmlloader.load());

            ThresholdController thresholdController = fxmlloader.getController();
            thresholdController.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Set Threshold");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            Products[] refreshedProducts = ProductManager.loadFromNewFile();
            productsTable.setItems(toObservableList(refreshedProducts));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Threshold Controller Error");
            alert.setHeaderText("Threshold Window Error");
            alert.setContentText("Error while opening Threshold window.");
            alert.showAndWait();
        }
    }
}
