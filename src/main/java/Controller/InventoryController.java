package Controller;

import Features.CartManager;
import Features.LowStock;
import Features.ProductManager;
import Features.Search;
import Model.Products;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private Label totalPartsLabel;

    @FXML
    private Label totalValueLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Button viewPicBtn;

    private static String currentKeyword = "";
    private static String currentCategory = "All";
    private static boolean currentLowStockOnly = false;
    private static Double currentMinPrice = null;
    private static Double currentMaxPrice = null;


    private void bubbleSortByCode(Products[] products) {
        int n = products.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (products[j].getCode().compareTo(products[j + 1].getCode()) > 0) {
                    Products temp = products[j];
                    products[j] = products[j + 1];
                    products[j + 1] = temp;
                }
            }
        }
    }


    private Products[] groupAndSortByCategory(Products[] products) {
        ArrayList<String> categoriesFound = new ArrayList<>();

        for (Products product : products) {
            if (!alreadyInList(categoriesFound, product.getCategory())) {
                categoriesFound.add(product.getCategory());
            }
        }

        ArrayList<Products> result = new ArrayList<>();

        for (String category : categoriesFound) {
            ArrayList<Products> sameCategoryItems = new ArrayList<>();

            for (Products product : products) {
                if (product.getCategory().equalsIgnoreCase(category)) {
                    sameCategoryItems.add(product);
                }
            }

            Products[] sameCategoryArray = sameCategoryItems.toArray(new Products[0]);
            bubbleSortByCode(sameCategoryArray);

            for (Products product : sameCategoryArray) {
                result.add(product);
            }
        }

        return result.toArray(new Products[0]);
    }


    private boolean alreadyInList(ArrayList<String> list, String value) {
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }


    private int calculateTotalParts(Products[] products) {
        int total = 0;
        for (Products product : products) {
            total += product.getQuantity();
        }
        return total;
    }


    private double calculateTotalValue(Products[] products) {
        double total = 0;
        for (Products product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }


    private ObservableList<Products> toObservableList(Products[] products) {
        ObservableList<Products> productlist = FXCollections.observableArrayList();
        if (products != null) {
            Products[] grouped = groupAndSortByCategory(products);
            productlist.addAll(grouped);
            totalPartsLabel.setText("Total Parts: " + calculateTotalParts(grouped));
            totalValueLabel.setText("Total Value: Rs. " + String.format("%.2f", calculateTotalValue(grouped)));
        }
        return productlist;
    }


    private void applyCurrentFiltersAndSearch() {
        Products[] allProducts = ProductManager.loadFromNewFile();
        Products[] result = Search.search(allProducts, searchField.getText(), currentLowStockOnly, currentMinPrice, currentMaxPrice, currentCategory);
        productsTable.setItems(toObservableList(result));
    }


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

            if (product.getQuantity()==0) {
                Stock = "Out of Stock";
            }

            return new SimpleStringProperty(Stock);
        });

        searchField.setText(currentKeyword);
        applyCurrentFiltersAndSearch();

        viewPicBtn.setVisible(false);
        viewPicBtn.setManaged(false);

        productsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null &&hasPic(newValue)){
                viewPicBtn.setVisible(true);
                viewPicBtn.setManaged(true);
            }else {
                viewPicBtn.setVisible(false);
                viewPicBtn.setManaged(false);
            }
        });
    }


    private boolean hasPic (Products product) {
        if (!product.getPicture().equals("null") && product.getPicture() != null && !product.getPicture().isEmpty()){
            return true;
        }else return false;
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


    @FXML
    private void onCartButtonClick() {
        Products product = productsTable.getSelectionModel().getSelectedItem();

        if (product == null ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart Message");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("Select a product before adding to cart.");
            alert.showAndWait();
            return;
        }

        if (product.getQuantity() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart Message");
            alert.setHeaderText("Product Out of Stock");
            alert.setContentText("Selected product is out of stock");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InventoryController.class.getResource("/FXML/Quantity.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AddToCartController addToCartController = fxmlLoader.getController();
            addToCartController.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Add To Cart");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

        }catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add To Cart Controller Error");
            alert.setHeaderText("Add To Cart Window Error");
            alert.setContentText("Error while opening Add To Cart window.");
            alert.showAndWait();
        }



    }


    @FXML
    private void onSearchButtonClick() {
        currentKeyword = searchField.getText();
        applyCurrentFiltersAndSearch();
    }


    @FXML
    private void onFilterButtonClick() {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(InventoryController.class.getResource("/FXML/Filter.fxml"));
            Scene scene = new Scene(fxmlloader.load());

            FilterController filterController = fxmlloader.getController();
            filterController.setAvailableCategories(ProductManager.loadFromNewFile());
            filterController.setFilter(currentCategory, currentLowStockOnly, currentMinPrice, currentMaxPrice);

            Stage stage = new Stage();
            stage.setTitle("Filter Products");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            if (filterController.wasConfirmed()) {
                currentCategory = filterController.getSelectedCategory();
                currentLowStockOnly = filterController.isLowStockOnly();
                currentMinPrice = filterController.getMinPrice();
                currentMaxPrice = filterController.getMaxPrice();
                applyCurrentFiltersAndSearch();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filter Controller Error");
            alert.setHeaderText("Filter Window Error");
            alert.setContentText("Error while opening Filter window.");
            alert.showAndWait();
        }
    }


    @FXML
    private void onPictureButtonClick(){
        Products product = productsTable.getSelectionModel().getSelectedItem();

        if (product == null || !hasPic(product)) {
            return;
        }

        File img = new File("src/main/resources/Additional/ItemPictures/" + product.getPicture());

        if (!img.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Image Loading Error");
            alert.setHeaderText("Image Loading Error");
            alert.setContentText("Image does not exist. ");
            alert.showAndWait();
            return;
        }

        Image image = new Image(img.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(400);

        StackPane imagePane = new StackPane(imageView);
        imagePane.setStyle("-fx-background-color: white; -fx-padding: 20;");

        Stage stage = new Stage();
        stage.setTitle(product.getCode() + " - " + product.getName());
        stage.setScene(new Scene(imagePane));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
