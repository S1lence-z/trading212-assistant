package app.store;

import app.models.DocumentsListItem;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class DocumentsListItemStore {
    private static DocumentsListItemStore instance = null;
    private List<DocumentsListItem> documentsList = new ArrayList<>();

    private DocumentsListItemStore() {}

    public static synchronized DocumentsListItemStore getInstance() {
        if (instance == null) {
            instance = new DocumentsListItemStore();
        }
        return instance;
    }

    public void populateDocumentListFromCacheAsync(ListView<DocumentsListItem> documentsList) {
        Task<Void> populateFromCacheTask = new Task<Void>() {
            @Override
            protected Void call() {
                documentsList.getItems().clear();
                documentsList.getItems().addAll(getDocumentsList());
                return null;
            }
        };
        new Thread(populateFromCacheTask).start();
    }

    public List<DocumentsListItem> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(JsonArray documentsList) {
        for (JsonElement document : documentsList) {
            DocumentsListItem item = new DocumentsListItem(document);
            this.documentsList.add(item);
        }
    }

    public boolean isEmpty() {
        return documentsList.isEmpty();
    }
}
