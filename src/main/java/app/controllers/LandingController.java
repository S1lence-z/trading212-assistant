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

/**
 * Controller class for the landing page of the application.
 * This class manages user interactions with the API key input,
 * as well as loading, saving, and proceeding to the next view.
 */
public class LandingController extends BaseController {
    
    /** Path to the documents view to switch to after setting the API key. */
    private static final String DOCUMENTS_VIEW = ViewManager.getDocumentsViewPath();

    /** TextField for entering the API key. */
    @FXML
    private TextField apiKeyInputField;

    /** Label displaying the API key text. */
    @FXML
    private Label apiKeyLabel;

    /** Label for displaying the application title. */
    @FXML
    private Text appTitleLabel;

    /** Button for loading a saved API key. */
    @FXML
    private Button loadKeyButton;

    /** Button for saving the entered API key. */
    @FXML
    private Button saveKeyButton;

    /** Button for proceeding to the next view if the API key is set. */
    @FXML
    private Button startButton;

    /**
     * Loads the API key from a file asynchronously and populates
     * the {@code apiKeyInputField} with the loaded key.
     *
     * @param event The action event triggered by clicking the load button.
     */
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

    /**
     * Saves the API key entered in {@code apiKeyInputField} asynchronously.
     * Displays a warning if no key is entered.
     *
     * @param event The action event triggered by clicking the save button.
     */
    @FXML
    void onClickSaveKey(ActionEvent event) {
        String userInput = apiKeyInputField.getText();
        if (userInput.isEmpty()) {
            AlertDialog.showWarning("No key to save", "Please enter a key to save");
            return;
        }
        KeySaver.saveKeyToFileAsync(userInput);
    }

    /**
     * Sets the API key if entered and proceeds to the documents view.
     * Shows a warning if no key is entered or if the key is already set.
     *
     * @param event The action event triggered by clicking the start button.
     * @throws Exception if switching to the documents view fails.
     */
    @FXML
    void onClickStart(ActionEvent event) throws Exception {
        String userInput = apiKeyInputField.getText();
        if (userInput.isEmpty()) {
            AlertDialog.showWarning("No key entered", "Please enter a key to continue");
            return;
        }
        boolean isKeySet = KeySaver.getInstance().setApiKey(userInput);
        if (!isKeySet) {
            AlertDialog.showWarning("Key already set", "Key has already been set");
            return;
        }
        switchToView(DOCUMENTS_VIEW);
    }
}
