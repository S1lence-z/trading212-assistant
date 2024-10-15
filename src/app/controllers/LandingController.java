package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.KeySaver;
import utils.ViewManager;

public class LandingController extends BaseController {
    private static final String DOCUMENTS_VIEW = ViewManager.getDocumentsViewPath();

    @FXML
    private TextField apiKeyInputField;

    @FXML
    private Button startButton;

    @FXML
    void onClickStartButton(ActionEvent event) throws Exception {
        String userInput = apiKeyInputField.getText();
        KeySaver.saveKeyToFile(userInput);
        switchToView(DOCUMENTS_VIEW);
    }
}
