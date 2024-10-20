package app.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import api.*;
import app.models.DocumentsListItem;
import utils.AlertDialog;

public class DocumentsController extends BaseController {
    @FXML
    private CheckBox DividendsBox;

    @FXML
    private ListView<DocumentsListItem> DocumentsList;

    private void setupDocumentsList() {
        DocumentsList.setCellFactory(param -> new ListCell<DocumentsListItem>() {
            private final Label reportIdLabel = new Label();
            private final Label timeFromLabel = new Label();
            private final Label timeToLabel = new Label();
            private final Label dataIncludedLabel = new Label();
            private final Label statusLabel = new Label();
            private final Button downloadButton = new Button("Download");
            private final HBox content;

            {
                HBox.setHgrow(reportIdLabel, Priority.ALWAYS);
                content = new HBox(reportIdLabel, timeFromLabel, timeToLabel, dataIncludedLabel, statusLabel, downloadButton);
                content.setSpacing(15);
                content.setStyle("-fx-padding: 10px;");

                reportIdLabel.setStyle("-fx-font-size: 14px;");
                timeFromLabel.setStyle("-fx-font-size: 14px;");
                timeToLabel.setStyle("-fx-font-size: 14px;");
                dataIncludedLabel.setStyle("-fx-font-size: 14px;");
                statusLabel.setStyle("-fx-font-size: 14px;");
                
                downloadButton.setOnAction(event -> {
                    DocumentsListItem item = getItem();
                    System.out.println("Downloading " + item.getReportId());
                    // TODO: CsvDownloader.downloadCsv(item.getDownloadLink());
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
                    setGraphic(content);
                }
            }
        });
    }

    private void populateDocumentsList(JsonArray dataArray) {
        for (JsonElement document : dataArray) {
            DocumentsListItem listItem = new DocumentsListItem(document);
            DocumentsList.getItems().add(listItem);
        }
    }

    private void populateDocumentsListAsync() {
        Task<JsonElement> getDocumentsListTask = new Task<JsonElement>() {
            @Override
            protected JsonElement call() throws Exception {
                JsonElement result = TradingApiCommunicator.getExportHistoryAsync().get();
                if (result.isJsonArray()) {
                    return result;
                } else if (result.isJsonObject()) {
                    throw new Exception("The server returned an error: " + result.getAsJsonObject().get("message").getAsString());
                } else {
                    throw new Exception("Unexpected JSON element type");
                }
            }

            @Override
            protected void succeeded() {
                JsonArray dataArray = getValue().getAsJsonArray();
                DocumentsList.getItems().clear();
                Platform.runLater(() -> {
                    populateDocumentsList(dataArray);
                });
            }

            @Override
            protected void failed() {
                String exceptionMessage = getException().getMessage();
                Platform.runLater(() -> {
                    AlertDialog.showError("Failed to get documents list", exceptionMessage);
                });
            }
        };

        new Thread(getDocumentsListTask).start();
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
    
    @FXML
    private Label ToDateLabel;
    
    @FXML
    private DatePicker ToDatePicker;
    
    @FXML
    private CheckBox TransactionsBox;
    
    @FXML
    void onActionRequestButton(ActionEvent event) {

    }
    
    public void initialize() {
        setupDocumentsList();
        populateDocumentsListAsync();
    }
    
    private void getExportHistoryAsync() {
        Task<JsonElement> getExportHistoryTask = new Task<JsonElement>() {
            @Override
            protected JsonElement call() throws Exception {
                return TradingApiCommunicator.getExportHistoryAsync().get();
            }
            
            @Override
            protected void succeeded() {
                JsonElement result = getValue();
                System.out.println(result);
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    AlertDialog.showError("Failed to get history", "Failed to get the history from the server.");
                });
            }
        };
        
        new Thread(getExportHistoryTask).start();
    }
    
    private void postExportHistoryAsync() {
        var fromDateRaw = FromDatePicker.getValue();
        if (fromDateRaw == null) {
            AlertDialog.showWarning("No from date selected", "Please select a date from which to export the history.");
            return;
        }
        var toDateRaw = ToDatePicker.getValue();
        if (toDateRaw == null) {
            AlertDialog.showWarning("No to date selected", "Please select a date to which to export the history.");
            return;
        }
        boolean includeDividends = DividendsBox.isSelected();
        boolean includeInterest = InterestBox.isSelected();
        boolean includeOrders = OrdersBox.isSelected();
        boolean includeTransactions = TransactionsBox.isSelected();
    
        String newRequestBody = TradingRequestFormatter.getBodyForPostExportHistory(fromDateRaw, toDateRaw, includeTransactions, includeOrders, includeDividends, includeInterest);
        TradingApiCommunicator.postExportHistoryAsync(newRequestBody);
    }
}
