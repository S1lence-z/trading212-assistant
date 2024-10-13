package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.KeySaver;

public class LandingController {
    @FXML
    private TextField apiKeyInputField;

    @FXML
    private Button startButton;

    @FXML
    void onClickStartButton(ActionEvent event) {
        String userInput = apiKeyInputField.getText();
        KeySaver.saveKey(userInput);
        if (KeySaver.keyExists()) {
            System.out.println("Key saved successfully!");
            
        } else {
            System.out.println("Key not saved!");
            return;
        }
    }

}
