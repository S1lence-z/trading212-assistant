package utils;

public final class ViewManager {
    private static final String VIEW_PATH_PREFIX = "/app/views/";
    private static final String VIEW_PATH_SUFFIX = ".fxml";

    public static String getMainLayoutPath() {
        return VIEW_PATH_PREFIX + "MainLayout" + VIEW_PATH_SUFFIX;
    }

    public static String getLandingViewPath() {
        return VIEW_PATH_PREFIX + "LandingView" + VIEW_PATH_SUFFIX;
    }

    public static String getDocumentsViewPath() {
        return VIEW_PATH_PREFIX + "DocumentsView" + VIEW_PATH_SUFFIX;
    }

    public static String getDocumentDetailsViewPath() {
        return VIEW_PATH_PREFIX + "DocumentDetailsView" + VIEW_PATH_SUFFIX;
    }
}
