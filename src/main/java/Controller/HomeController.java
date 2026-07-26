package Controller;

import Cleaner.TextFileManager;
import Features.DealerManager;
import Model.Dealers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import Features.ProductManager;
import Model.Products;
import Features.LowStock;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class HomeController {
    @FXML
    private Label DealerCountLbl;

    @FXML
    private Label TotalItemsLbl;

    @FXML
    private Label LowStockLbl;

    @FXML
    private ListView<String> ActivityListLbl;

    @FXML
    private void initialize() {
        int totalItemCount = 0;
        int lowStockCount = 0;
        int dealerCount = 0;

        Products[] productsList = ProductManager.loadFromNewFile();
        Dealers[] dealerList = DealerManager.load();

        if (productsList != null) {
            for (Products products1 : productsList) {
                totalItemCount += products1.getQuantity();
                if (LowStock.isLowStock(products1)){
                    lowStockCount+=1;
                }
            }
        }

        if (dealerList != null) {
            dealerCount = dealerList.length;
        }

        TextFileManager textFileManager = new TextFileManager();
        String[] AuditLogLines = textFileManager.read("audit_log.txt");

        ObservableList<String> recentActivityList= FXCollections.observableArrayList();

        if (AuditLogLines != null) {
            int start = Math.max(0, AuditLogLines.length - 9);
            for (int i = AuditLogLines.length - 1 ; i >= start; i--) {
                recentActivityList.add(AuditLogLines[i]);
            }
        }

        if (recentActivityList.isEmpty()) {
            recentActivityList.add("No Recent Activity");
        }

        TotalItemsLbl.setText(String.valueOf(totalItemCount));
        LowStockLbl.setText(String.valueOf(lowStockCount));
        DealerCountLbl.setText(String.valueOf(dealerCount));
        ActivityListLbl.setItems(recentActivityList);
    }
}
