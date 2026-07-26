package Controller;

import Features.DealerManager;
import Model.Dealers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DealerController {
    @FXML
    private TableView<Dealers> dealerTable;

    @FXML
    private TableColumn<Dealers, String> codeColumn;

    @FXML
    private TableColumn<Dealers, String> nameColumn;

    @FXML
    private TableColumn<Dealers, String> contactColumn;

    @FXML
    private TableColumn<Dealers, String> locationColumn;

    @FXML
    private void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadAllDealers();
    }

    private void loadAllDealers() {
        Dealers[] dealers = DealerManager.load();
        dealerTable.setItems(toObservableList(dealers));
    }

    private ObservableList<Dealers> toObservableList(Dealers[] dealers) {
        ObservableList<Dealers> dealerList = FXCollections.observableArrayList();
        if (dealers != null) {
            dealerList.addAll(dealers);
        }
        return dealerList;
    }

    @FXML
    private void onRandomSelectClick() {
        Dealers[] randomDealers = DealerManager.DealerRandomSelect();
        dealerTable.setItems(toObservableList(randomDealers));
    }

    @FXML
    private void onResetClick() {
        loadAllDealers();
    }
}
