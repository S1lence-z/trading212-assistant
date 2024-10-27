package app.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public abstract class BaseController {
    protected AnchorPane mainContent;

    public void setMainContent(AnchorPane mainContent) {
        this.mainContent = mainContent;
    }

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
