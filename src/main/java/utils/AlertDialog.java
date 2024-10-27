package utils;

import javafx.scene.control.Alert;

/**
 * Utility class for displaying alert dialogs in the application.
 */
public class AlertDialog {
    
    /**
     * Displays a warning alert dialog with the specified title and message.
     *
     * @param title   The title of the warning dialog.
     * @param message The message to be displayed in the warning dialog.
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an error alert dialog with the specified title and message.
     *
     * @param title   The title of the error dialog.
     * @param message The message to be displayed in the error dialog.
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an information alert dialog with the specified title and message.
     *
     * @param title   The title of the information dialog.
     * @param message The message to be displayed in the information dialog.
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
