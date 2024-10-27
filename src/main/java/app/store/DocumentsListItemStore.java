package app.store;

import app.models.DocumentsListItem;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Singleton class that manages a list of {@link DocumentsListItem} instances.
 * Handles populating the document list asynchronously and provides methods
 * to interact with the list of documents.
 */
public class DocumentsListItemStore {
    /** Singleton instance of the store. */
    private static DocumentsListItemStore instance = null;

    /** List of document items. */
    private List<DocumentsListItem> documentsList = new ArrayList<>();

    /** Private constructor to enforce singleton pattern. */
    private DocumentsListItemStore() {}

    /**
     * Retrieves the singleton instance of the DocumentsListItemStore.
     *
     * @return The singleton instance of the store.
     */
    public static synchronized DocumentsListItemStore getInstance() {
        if (instance == null) {
            instance = new DocumentsListItemStore();
        }
        return instance;
    }

    /**
     * Asynchronously populates the specified ListView with cached document items.
     *
     * @param documentsListView The ListView to populate with document items.
     */
    public void populateDocumentListFromCacheAsync(ListView<DocumentsListItem> documentsListView) {
        Task<Void> populateFromCacheTask = new Task<Void>() {
            @Override
            protected Void call() {
                documentsListView.getItems().clear();
                documentsListView.getItems().addAll(getDocumentsList());
                return null;
            }
        };
        new Thread(populateFromCacheTask).start();
    }

    /**
     * Retrieves the current list of document items.
     *
     * @return The list of document items.
     */
    public List<DocumentsListItem> getDocumentsList() {
        return documentsList;
    }

    /**
     * Sets the list of document items from a JSON array.
     * Each JSON element in the array is converted into a {@link DocumentsListItem}.
     *
     * @param documentsListJson The JSON array containing document items.
     */
    public void setDocumentsList(JsonArray documentsListJson) {
        for (JsonElement document : documentsListJson) {
            DocumentsListItem item = new DocumentsListItem(document);
            this.documentsList.add(item);
        }
    }

    /**
     * Checks if the document list is empty.
     *
     * @return true if the document list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return documentsList.isEmpty();
    }
}
