package app.controllers;

import com.google.gson.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import api.*;
import utils.AlertDialog;
import utils.CountDownTimer;
import utils.CsvManager;
import utils.ViewManager;
import app.models.DocumentsListItem;
import app.store.DocumentsListItemStore;

/**
 * DocumentsController is responsible for managing and displaying a list of documents
 * in the application. It provides functionality to view document details, download
 * CSV files asynchronously, and request document history exports.
 */
public class DocumentsController extends BaseController {
    private static final String DOCUMENT_DETAILS_VIEW = ViewManager.getDocumentDetailsViewPath();
    private static final DocumentsListItemStore documentsListItemStore = DocumentsListItemStore.getInstance();

    @FXML
    private CheckBox DividendsBox;

    @FXML
    private ListView<DocumentsListItem> DocumentsList;

    /**
     * Sets up the document list view, defining the cell structure and adding
     * buttons for each document entry to download or show the document details.
     */
    private void setupDocumentsList() {
        DocumentsList.setCellFactory(param -> new ListCell<DocumentsListItem>() {
            private final Label reportIdLabel = new Label();
            private final Label timeFromLabel = new Label();
            private final Label timeToLabel = new Label();
            private final Label dataIncludedLabel = new Label();
            private final Label statusLabel = new Label();
            private final Button downloadButton = new Button("Download");
            private final Button showButton = new Button("Show");
            private final Label downloadedStateLabel = new Label("");
            private final HBox content;

            {
                HBox.setHgrow(reportIdLabel, Priority.ALWAYS);
                content = new HBox(reportIdLabel, timeFromLabel, timeToLabel, dataIncludedLabel, statusLabel, downloadButton, downloadedStateLabel, showButton);

                // Styling
                content.setSpacing(15);
                content.setStyle("-fx-padding: 10px;");
                reportIdLabel.setStyle("-fx-font-size: 18px;");
                timeFromLabel.setStyle("-fx-font-size: 18px;");
                timeToLabel.setStyle("-fx-font-size: 18px;");
                dataIncludedLabel.setStyle("-fx-font-size: 18px;");
                statusLabel.setStyle("-fx-font-size: 18px;");
                downloadButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
                showButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");
                downloadedStateLabel.setStyle("-fx-font-size: 18px;");
                
                // Event handlers
                downloadButton.setOnAction(event -> {
                    DocumentsListItem item = getItem();
                    String downloadLink = item.getDownloadLink();
                    String reportId = Integer.toString(item.getReportId());
                    downloadCsvAsync(downloadLink, reportId);
                });

                showButton.setOnAction(event -> {
                    try {
                        DocumentsListItem item = getItem();
                        String reportId = Integer.toString(item.getReportId());
                        CsvManager.parseCsvFile(reportId);
                        switchToView(DOCUMENT_DETAILS_VIEW);
                    } catch (Exception e) {
                        AlertDialog.showError("Failed to parse CSV", e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(DocumentsListItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    reportIdLabel.setText("Report ID: " + item.getReportId());
                    timeFromLabel.setText("From: " + item.getTimeFrom());
                    timeToLabel.setText("To: " + item.getTimeTo());
                    dataIncludedLabel.setText(item.getDataIncluded().toString());
                    statusLabel.setText("Status: " + item.getStatus());
                    downloadedStateLabel.setText(CsvManager.isFileDownloaded(Integer.toString(item.getReportId())) ? "✅" : "❌");
                    setGraphic(content);
                }
            }
        });
    }

    /**
     * Populates the documents list asynchronously. If cached data exists and fetching
     * new data is not required, it loads from cache; otherwise, it fetches from the API.
     *
     * @param mustFetchNewData specifies if new data must be fetched from the API
     */
    private void populateDocumentsListAsync(boolean mustFetchNewData) {
        if (!documentsListItemStore.isEmpty() && !mustFetchNewData) {
            documentsListItemStore.populateDocumentListFromCacheAsync(DocumentsList);
            return;
        }
        TradingApiCommunicator.getExportHistoryAsync()
        .thenAccept(result -> {
            if (result.isJsonArray()) {
                JsonArray dataArray = result.getAsJsonArray();
                documentsListItemStore.setDocumentsList(dataArray);
                Platform.runLater(() -> {
                    DocumentsList.getItems().clear();
                    populateDocumentsList(dataArray);
                });
            } else if (result.isJsonObject()) {
                String errorMessage = result.getAsJsonObject().get("errorMessage").getAsString();
                Platform.runLater(() -> {
                    AlertDialog.showError("Failed to get documents list", errorMessage);
                });
            }
        })
        .exceptionally(ex -> {
            Platform.runLater(() -> {
                AlertDialog.showError("Failed to get documents list", ex.getMessage());
            });
            return null;
        });
    }

    /**
     * Populates the documents list with data from the provided JSON array.
     *
     * @param dataArray a JsonArray containing document data
     */
    private void populateDocumentsList(JsonArray dataArray) {
        for (JsonElement document : dataArray) {
            DocumentsListItem listItem = new DocumentsListItem(document);
            DocumentsList.getItems().add(listItem);
        }
    }

    /**
     * Downloads a CSV file asynchronously using the provided download link and report ID.
     *
     * @param downloadLink the URL link to download the CSV
     * @param reportId     the report ID for the downloaded CSV
     */
    private void downloadCsvAsync(String downloadLink, String reportId) {
        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                CsvManager.downloadCsvFile(downloadLink, reportId);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    AlertDialog.showInfo("Downloaded CSV", "The CSV file has been downloaded successfully.");
                });
            }

            @Override
            protected void failed() {
                String exceptionMessage = getException().getMessage();
                Platform.runLater(() -> {
                    AlertDialog.showError("Failed to download CSV", exceptionMessage);
                });
            }
        };

        new Thread(downloadTask).start();
    }

    @FXML
    private VBox FormAndListSection;

    @FXML
    private Label FromDateLabel;

    @FXML
    private DatePicker FromDatePicker;

    @FXML
    private Label IncludeLabel;

    @FXML
    private CheckBox InterestBox;

    @FXML
    private CheckBox OrdersBox;
    
    @FXML
    private HBox PageLayout;
    
    @FXML
    private Button RequestButton;

    /**
     * Handles the action for the request button to initiate a new document export request.
     *
     * @param event the ActionEvent triggered by clicking the request button
     */
    @FXML
    void onActionRequestButton(ActionEvent event) {
        String newRequestBody = getBodyPostExportHistory();
        postExportHistoryAsync(newRequestBody);
    }

    /**
     * Sends a request to the API to initiate a document export history asynchronously.
     *
     * @param requestBody the body of the request to be sent to the API
     */
    private void postExportHistoryAsync(String requestBody) {
        TradingApiCommunicator.postExportHistoryAsync(requestBody).thenAccept(result -> {
            if (result.has("reportId")) {
                Platform.runLater(() -> {
                    CountDownTimer.startTimer(90, timerLabel, () -> {
                        populateDocumentsListAsync(true);
                    });
                });
            } else {
                String errorMessage = result.has("errorMessage") ? result.get("errorMessage").getAsString() : result.toString();
                Platform.runLater(() -> {
                    AlertDialog.showError("Failed to send export history request", errorMessage);
                });
            } 
        })
        .exceptionally(ex -> {
            ex.printStackTrace();
            Platform.runLater(() -> {
                AlertDialog.showError("Failed to request the CSV", ex.getMessage());
            });
            return null;
        });
    }

    /**
     * Generates the request body for posting a new export history based on selected dates
     * and options (like including dividends, interest, orders, or transactions).
     *
     * @return the JSON-formatted request body string, or null if validation fails
     */
    private String getBodyPostExportHistory() {
        var fromDateRaw = FromDatePicker.getValue();
        if (fromDateRaw == null) {
            AlertDialog.showWarning("No from date selected", "Please select a date from which to export the history.");
            return null;
        }
        var toDateRaw = ToDatePicker.getValue();
        if (toDateRaw == null) {
            AlertDialog.showWarning("No to date selected", "Please select a date to which to export the history.");
            return null;
        }
        boolean includeDividends = DividendsBox.isSelected();
        boolean includeInterest = InterestBox.isSelected();
        boolean includeOrders = OrdersBox.isSelected();
        boolean includeTransactions = TransactionsBox.isSelected();
        return TradingRequestFormatter.getBodyForPostExportHistory(fromDateRaw, toDateRaw, includeTransactions, includeOrders, includeDividends, includeInterest);
    }
    
    @FXML
    private Label ToDateLabel;
    
    @FXML
    private DatePicker ToDatePicker;
    
    @FXML
    private CheckBox TransactionsBox;

    @FXML
    private Label timerLabel;

    /**
     * Initializes the controller by setting up the documents list and populating it asynchronously.
     */
    public void initialize() {
        setupDocumentsList();
        populateDocumentsListAsync(false);
    }
}
