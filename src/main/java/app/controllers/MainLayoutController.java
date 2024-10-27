package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import utils.ViewManager;

public class MainLayoutController extends BaseController {
    protected final String LANDING_VIEW = ViewManager.getLandingViewPath();

    @FXML
    private AnchorPane mainContent;

    @FXML
    public void initialize() throws Exception {
        setMainContent(mainContent);
        switchToView(LANDING_VIEW);
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }
}
