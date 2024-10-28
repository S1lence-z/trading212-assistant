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

/**
 * DocumentDetailsController is responsible for managing the view that displays
 * detailed information about various documents, including summaries and lists of
 * transactions, dividends, interest, and orders. It also allows switching back to
 * the main documents view.
 */
public class DocumentDetailsController extends BaseController {
    private static final String DOCUMENTS_VIEW = ViewManager.getDocumentsViewPath();

    @FXML
    private Button backButton;

    @FXML
    private Label depositsLabel;

    @FXML
    private ListView<String> documentDetailsList;

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

    /**
     * Initializes the controller by setting up the document details list and loading
     * both summary and detailed data asynchronously.
     */
    public void initialize() {
        setupDocumentDetailsList();
        showSummaryDataAsync();
        showAllDataAsync();
    }

    /**
     * Handles the action for the back button, switching the view back to the main documents view.
     *
     * @param event the ActionEvent triggered by clicking the back button
     * @throws Exception if an error occurs during the view switch
     */
    @FXML
    void onActionBackButton(ActionEvent event) throws Exception {
        switchToView(DOCUMENTS_VIEW);
    }

    /**
     * Sets up the document details list with custom cell rendering to display detailed data.
     */
    private void setupDocumentDetailsList() {
        documentDetailsList.setCellFactory(param -> new ListCell<String>() {
            private final Label label = new Label();
            private final HBox content;

            {
                HBox.setHgrow(label, Priority.ALWAYS);
                content = new HBox(label);
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

    /**
     * Asynchronously loads and displays all data (detailed transactions, dividends,
     * interest, and orders) in the document details list.
     */
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

    /**
     * Loads all detailed data for dividends, transactions, interest, and orders,
     * and populates the document details list with this data.
     */
    private void showAllData() {
        HashMap<String, String> allDividendsData = DividendsParser.getInstance().getAllData();
        HashMap<String, String> allTransactionsData = TransactionsParser.getInstance().getAllData();
        HashMap<String, String> allInterestData = InterestParser.getInstance().getAllData();
        HashMap<String, String> allOrdersData = OrdersParser.getInstance().getAllData();

        documentDetailsList.getItems().add("Dividends:");
        allDividendsData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Transactions:");
        allTransactionsData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Interest:");
        allInterestData.forEach((key, value) -> documentDetailsList.getItems().add(value));
        documentDetailsList.getItems().add("Orders:");
        allOrdersData.forEach((key, value) -> documentDetailsList.getItems().add(value));
    }

    /**
     * Asynchronously loads and displays summary data for transactions, income,
     * expenses, profits, dividends, and interest.
     */
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

    /**
     * Loads summarized data for dividends, transactions, interest, and orders,
     * and sets the values in corresponding labels on the view.
     */
    private void showSummaryData() {
        HashMap<String, String> dividendsData = DividendsParser.getInstance().getSummarizedData();
        HashMap<String, HashMap<String, String>> transactionsData = TransactionsParser.getInstance().getSummarizedData();
        HashMap<String, String> interestData = InterestParser.getInstance().getSummarizedData();
        HashMap<String, HashMap<String, String>> ordersData = OrdersParser.getInstance().getSummarizedData();
        setSummarizedTransactions(transactionsData);
        setSummarizedOrders(ordersData);
        setSummarizedDividends(dividendsData);
        setSummarizedInterest(interestData);
    }

    private void setSummarizedTransactions(HashMap<String, HashMap<String, String>> transactionsData) {
        StringBuilder depositsSb = new StringBuilder();
        StringBuilder withdrawalsSb = new StringBuilder();
        for (String currency : transactionsData.keySet()) {
            HashMap<String, String> currencyData = transactionsData.get(currency);
            String totalDeposits = currencyData.get("totalDeposits");
            String totalWithdrawals = currencyData.get("totalWithdrawals");
            if (totalDeposits != null) {
                depositsSb.append(totalDeposits).append(" ").append(currency).append("\n");
            }
            if (totalWithdrawals != null) {
                withdrawalsSb.append(totalWithdrawals).append(" ").append(currency).append("\n");
            }
        }
        depositsLabel.setText(depositsSb.toString());
        withdrawalsLabel.setText(withdrawalsSb.toString());
    }

    private void setSummarizedOrders(HashMap<String, HashMap<String, String>> ordersData) {
        StringBuilder incomeSb = new StringBuilder();
        StringBuilder expensesSb = new StringBuilder();
        StringBuilder profitSb = new StringBuilder();
        for (String currency : ordersData.keySet()) {
            HashMap<String, String> currencyData = ordersData.get(currency);
            String totalIncome = currencyData.get("totalIncome");
            String totalExpenses = currencyData.get("totalExpenses");
            String totalProfit = currencyData.get("totalProfit");
            incomeSb.append(totalIncome).append(" ").append(currency).append("\n");
            expensesSb.append(totalExpenses).append(" ").append(currency).append("\n");
            profitSb.append(totalProfit).append(" ").append(currency).append("\n");
        }
        incomeLabel.setText(incomeSb.toString());
        expensesLabel.setText(expensesSb.toString());
        profitLabel.setText(profitSb.toString());
    }

    private void setSummarizedDividends(HashMap<String, String> dividendsData) {
        StringBuilder dividendsSb = new StringBuilder();
        for (String currency : dividendsData.keySet()) {
            String dividendsValue = dividendsData.get(currency);
            dividendsSb.append(dividendsValue).append(" ").append(currency).append("\n");
        }
        totalDividendsLabel.setText(dividendsSb.toString());
    }

    private void setSummarizedInterest(HashMap<String, String> interestData) {
        StringBuilder interestSb = new StringBuilder();
        for (String currency : interestData.keySet()) {
            String interestValue = interestData.get(currency);
            interestSb.append(interestValue).append(" ").append(currency).append("\n");
        }
        totalInterestLabel.setText(interestSb.toString());
    }
}
