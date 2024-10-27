package utils;

/**
 * Utility class for managing and providing paths to FXML views in the application.
 * This class is designed to centralize the view path management for easier maintenance and readability.
 */
public final class ViewManager {
    private static final String VIEW_PATH_PREFIX = "/views/";
    private static final String VIEW_PATH_SUFFIX = ".fxml";

    /**
     * Returns the path to the main layout FXML file.
     *
     * @return The path as a String to the main layout view.
     */
    public static String getMainLayoutPath() {
        return VIEW_PATH_PREFIX + "MainLayout" + VIEW_PATH_SUFFIX;
    }

    /**
     * Returns the path to the landing view FXML file.
     *
     * @return The path as a String to the landing view.
     */
    public static String getLandingViewPath() {
        return VIEW_PATH_PREFIX + "LandingView" + VIEW_PATH_SUFFIX;
    }

    /**
     * Returns the path to the documents view FXML file.
     *
     * @return The path as a String to the documents view.
     */
    public static String getDocumentsViewPath() {
        return VIEW_PATH_PREFIX + "DocumentsView" + VIEW_PATH_SUFFIX;
    }

    /**
     * Returns the path to the document details view FXML file.
     *
     * @return The path as a String to the document details view.
     */
    public static String getDocumentDetailsViewPath() {
        return VIEW_PATH_PREFIX + "DocumentDetailsView" + VIEW_PATH_SUFFIX;
    }
}
