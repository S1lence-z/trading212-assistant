package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ViewManager;

public class Main extends Application {
    private static final String APP_TITLE = "Trading212 Assistant";
    private static final String MAIN_LAYOUT = ViewManager.getMainLayoutPath();

    private void setupStartStage(Stage mainScene, Parent root) {
        mainScene.setTitle(APP_TITLE);
        mainScene.setScene(new Scene(root));
        mainScene.setResizable(false);
        mainScene.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_LAYOUT));
        Parent root = loader.load();
        setupStartStage(primaryStage, root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
