package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ViewManager;

/**
 * Entry point for the Trading212 Assistant application.
 * Initializes the JavaFX application and loads the main layout.
 */
public class Main extends Application {

    /** Title of the application window. */
    private static final String APP_TITLE = "Trading212 Assistant";

    /** Path to the main layout view. */
    private static final String MAIN_LAYOUT = ViewManager.getMainLayoutPath();

    /**
     * Sets up and displays the main stage (window) with the specified title,
     * scene, and layout properties.
     *
     * @param mainScene The primary stage of the application.
     * @param root The root node of the scene to be displayed.
     */
    private void setupStartStage(Stage mainScene, Parent root) {
        mainScene.setTitle(APP_TITLE);
        mainScene.setScene(new Scene(root));
        mainScene.setResizable(false);
        mainScene.show();
    }

    /**
     * Starts the JavaFX application by loading the main layout and setting up the primary stage.
     *
     * @param primaryStage The primary stage for this JavaFX application.
     * @throws Exception if loading the main layout fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_LAYOUT));
        Parent root = loader.load();
        setupStartStage(primaryStage, root);
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
