package app.controllers;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import utils.ViewManager;
import utils.parsers.*;

public class DocumentDetailsController extends BaseController {
    private static final String DOCUMENTS_VIEW = ViewManager.getDocumentsViewPath();

    @FXML
    private Button backButton;

    @FXML
    void onActionBackButton(ActionEvent event) throws Exception {
        switchToView(DOCUMENTS_VIEW);
    }

    @FXML
    private Label depositsLabel;

    @FXML
    private ListView<String> documentDetailsList;

    private void setupDocumentDetailsList() {
        documentDetailsList.setCellFactory(param -> new ListCell<String>() {
            private final Label label = new Label();
            private final HBox content;

            {
                HBox.setHgrow(label, Priority.ALWAYS);
                content = new HBox(label);

                // Styling
                content.setSpacing(15);
                content.setStyle("-fx-padding: 10px;");
                label.setStyle("-fx-font-size: 16px;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(content);
                }
            }
        });        
    }

    @FXML
    private Label expensesLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label pageTitle;

    @FXML
    private Label profitLabel;

    @FXML
    private Label totalDividendsLabel;

    @FXML
    private Label totalInterestLabel;

    @FXML
    private AnchorPane transactionsContainer;

    @FXML
    private Label withdrawalsLabel;

    public void initialize() {
        setupDocumentDetailsList();
        showSummaryDataAsync();
        showAllDataAsync();
    }

    private void showAllDataAsync() {
        Task<Void> showParsedDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                showAllData();
                return null;
            }
        };
        new Thread(showParsedDataTask).start();
    }

    private void showAllData() {
        HashMap<String, String> allDividendsData = DividendsParser.getInstance().getAllData();
        HashMap<String, String> allTransactionsData = TransactionsParser.getInstance().getAllData();
        HashMap<String, String> allInterestData = InterestParser.getInstance().getAllData();
        HashMap<String, String> allOrdersData = OrdersParser.getInstance().getAllData();

        // populate the list
        documentDetailsList.getItems().add("Dividends:");
        allDividendsData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Transactions:");
        allTransactionsData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Interest:");
        allInterestData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Orders:");
        allOrdersData.forEach((key, value) -> documentDetailsList.getItems().add(value));
    }

    private void showSummaryDataAsync() {
        Task<Void> showParsedDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                showSummaryData();
                return null;
            }
        };
        new Thread(showParsedDataTask).start();
    }

    private void showSummaryData() {
        HashMap<String, String> dividendsData = DividendsParser.getInstance().getSummarizedData();
        HashMap<String, String> transactionsData = TransactionsParser.getInstance().getSummarizedData();
        HashMap<String, String> interestData = InterestParser.getInstance().getSummarizedData();
        HashMap<String, String> ordersData = OrdersParser.getInstance().getSummarizedData();
        // transactions
        depositsLabel.setText(transactionsData.get("totalDeposits"));
        withdrawalsLabel.setText(transactionsData.get("totalWithdrawals"));
        incomeLabel.setText(ordersData.get("totalIncome"));
        expensesLabel.setText(ordersData.get("totalExpenses"));
        profitLabel.setText(ordersData.get("totalProfit"));
        totalDividendsLabel.setText(dividendsData.get("totalDividends"));
        totalInterestLabel.setText(interestData.get("totalInterest"));
    }
}
