package app.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * BaseController is an abstract class that provides basic functionality for managing
 * and switching views in a JavaFX application. It utilizes asynchronous tasks to load
 * FXML views and set them in the main content area.
 */
public abstract class BaseController {
    
    /**
     * The main content area where views will be displayed.
     */
    protected AnchorPane mainContent;

    /**
     * Sets the main content area for displaying views.
     *
     * @param mainContent the AnchorPane where views will be displayed
     */
    public void setMainContent(AnchorPane mainContent) {
        this.mainContent = mainContent;
    }

    /**
     * Switches the view to the specified FXML file by loading it asynchronously.
     *
     * @param fxmlFile the path to the FXML file to load
     * @throws Exception if there is an error loading the FXML file
     */
    public void switchToView(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Task<Node> task = switchToViewTask(loader);

        task.setOnSucceeded(event -> {
            try {
                Node view = task.getValue();
                BaseController controller = loader.getController();
                controller.setMainContent(mainContent);
                mainContent.getChildren().setAll(view);
            } catch (Exception e) {
                System.out.println("Error during view switch: " + e);
                e.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            System.out.println("Failed to switch view: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    /**
     * Creates a Task to load the FXML view specified in the FXMLLoader.
     *
     * @param loader the FXMLLoader that will load the view
     * @return a Task that loads the view when executed
     */
    private Task<Node> switchToViewTask(FXMLLoader loader) {
        return new Task<Node>() {
            @Override
            protected Node call() throws Exception {
                if (mainContent == null) {
                    throw new Exception("Main content is not set");
                }
                return loader.load();
            }
        };
    }
}
