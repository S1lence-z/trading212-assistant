package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import utils.ViewManager;

/**
 * Controller class for the main layout of the application.
 * This class manages the main content display and initializes
 * the landing view.
 */
public class MainLayoutController extends BaseController {

    /** Path to the landing view to display on application startup. */
    protected final String LANDING_VIEW = ViewManager.getLandingViewPath();

    /** Main content area in the application layout. */
    @FXML
    private AnchorPane mainContent;

    /**
     * Initializes the main layout by setting the main content
     * area and switching to the landing view.
     *
     * @throws Exception if loading the landing view fails.
     */
    @FXML
    public void initialize() throws Exception {
        setMainContent(mainContent);
        switchToView(LANDING_VIEW);
    }

    /**
     * Exits the application.
     * This method is typically called when the user selects an exit option.
     */
    @FXML
    public void handleExit() {
        System.exit(0);
    }
}
