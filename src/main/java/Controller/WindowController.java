package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class WindowController {

    @FXML
    private Label help;

    @FXML
    protected void onHelpClick(){help.setText("Welcome!");
    }
}