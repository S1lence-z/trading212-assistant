package app.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import utils.AlertDialog;
import utils.KeySaver;
import utils.ViewManager;

public class LandingController extends BaseController {
    private static final String DOCUMENTS_VIEW = ViewManager.getDocumentsViewPath();

    @FXML
    private TextField apiKeyInputField;

    @FXML
    private Label apiKeyLabel;

    @FXML
    private Text appTitleLabel;

    @FXML
    private Button loadKeyButton;

    @FXML
    private Button saveKeyButton;

    @FXML
    private Button startButton;

    @FXML
    void onClickLoadKey(ActionEvent event) {
        Task<String> loadKeyTask = KeySaver.loadKeyFromFileAsync();
        loadKeyTask.setOnSucceeded(e -> {
            String key = loadKeyTask.getValue();
            apiKeyInputField.setText(key);
        });
        loadKeyTask.setOnFailed(e -> {
            String exceptionMessage = loadKeyTask.getException().getMessage();
            AlertDialog.showError("Failed to load key", exceptionMessage);
        });
    }

    @FXML
    void onClickSaveKey(ActionEvent event) {
        String userInput = apiKeyInputField.getText();
        if (userInput.isEmpty()) {
            AlertDialog.showWarning("No key to save", "Please enter a key to save");
            return;
        }
        KeySaver.saveKeyToFileAsync(userInput);
    }

    @FXML
    void onClickStart(ActionEvent event) throws Exception {
        String userInput = apiKeyInputField.getText();
        if (userInput.isEmpty()) {
            AlertDialog.showWarning("No key entered", "Please enter a key to continue");
            return;
        }
        boolean isKeySet = KeySaver.setApiKey(userInput);
        if (!isKeySet) {
            AlertDialog.showWarning("Key already set", "Key has already been set");
            return;
        }
        switchToView(DOCUMENTS_VIEW);
    }
}
