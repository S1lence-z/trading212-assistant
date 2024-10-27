package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing order data from CSV lines.
 * This class processes buy and sell orders, tracking total income, expenses, and profit.
 */
public class OrdersParser extends Parser {
    private static OrdersParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalIncome = 0.0;
    private Double totalExpenses = 0.0;
    private Double totalProfit = 0.0;
    private Dictionary<String, Integer> headerMap;

    /**
     * Retrieves the total income.
     *
     * @return the total income as a Double.
     */
    public Double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Retrieves the total expenses.
     *
     * @return the total expenses as a Double.
     */
    public Double getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * Retrieves the total profit.
     *
     * @return the total profit as a Double.
     */
    public Double getTotalProfit() {
        return totalProfit;
    }

    private OrdersParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the OrdersParser.
     *
     * @return the single instance of OrdersParser.
     */
    public static synchronized OrdersParser getInstance() {
        if (instance == null) {
            instance = new OrdersParser();
        }
        return instance;
    }

    /**
     * Parses a line of order data from the CSV file.
     *
     * @param line a String representing a line from the CSV file.
     * @throws RuntimeException if the header map is not set or if an invalid action type is found.
     */
    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for OrdersParser");
        }
        int actionIndex = this.headerMap.get("Action");
        int nameIndex = this.headerMap.get("Name"); 
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");

        if (line.contains("buy")) {
            handleBuyOrder(actionIndex, nameIndex, totalIndex, currencyIndex, data);
            return;
        } 
        if (line.contains("sell")) {
            handleSellOrder(actionIndex, nameIndex, totalIndex, currencyIndex, data);
            return;
        }
        throw new RuntimeException("Invalid action type in OrdersParser");
    }

    //! Sell order logic
    /**
     * Handles the logic for processing sell orders.
     *
     * @param actionIndex   the index of the action in the CSV data.
     * @param nameIndex     the index of the name in the CSV data.
     * @param totalIndex    the index of the total in the CSV data.
     * @param currencyIndex the index of the currency in the CSV data.
     * @param data          the parsed line data as an array of Strings.
     */
    private void handleSellOrder(int actionIndex, int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[actionIndex] + delimiter + data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalIncome(totalIndex, data);
        updateTotalProfit();
    }

    /**
     * Updates the total income based on a sell order.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalIncome(int totalIndex, String[] data) {
        this.totalIncome += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalIncome", formatNumberValue(String.valueOf(this.totalIncome)));
        updateTotalProfit();
    }

    //! Buy order logic
    /**
     * Handles the logic for processing buy orders.
     *
     * @param actionIndex   the index of the action in the CSV data.
     * @param nameIndex     the index of the name in the CSV data.
     * @param totalIndex    the index of the total in the CSV data.
     * @param currencyIndex the index of the currency in the CSV data.
     * @param data          the parsed line data as an array of Strings.
     */
    private void handleBuyOrder(int actionIndex, int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[actionIndex] + delimiter + data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalExpenses(totalIndex, data);
    }

    /**
     * Updates the total expenses based on a buy order.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalExpenses(int totalIndex, String[] data) {
        //? Calculate the total amount of expenses
        this.totalExpenses += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalExpenses", formatNumberValue(String.valueOf(this.totalExpenses)));
        updateTotalProfit();
    }

    /**
     * Updates the total profit based on current income and expenses.
     */
    private void updateTotalProfit() {
        //? Calculate the total profit
        this.totalProfit = this.totalIncome - this.totalExpenses;
        this.summarizedData.put("totalProfit", formatNumberValue(String.valueOf(this.totalProfit)));
    }

    /**
     * Retrieves all parsed order data.
     *
     * @return a HashMap containing all parsed order data.
     */
    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    /**
     * Retrieves summarized order data.
     *
     * @return a HashMap containing summarized order data.
     */
    @Override
    public HashMap<String, String> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all stored order data and resets totals.
     */
    @Override
    public void clearData() {
        this.totalIncome = 0.0;
        this.totalExpenses = 0.0;
        this.totalProfit = 0.0;
        this.allData.clear();
        this.summarizedData.clear();
    }

    /**
     * Sets the header mapping for the parser.
     *
     * @param headerMap a Dictionary mapping header names to their indices.
     */
    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
