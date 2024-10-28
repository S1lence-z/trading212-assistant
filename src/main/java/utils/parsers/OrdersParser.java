package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing order data from CSV lines.
 * This class processes buy and sell orders, tracking total income, expenses, and profit.
 */
public class OrdersParser extends Parser<HashMap<String, String>> {
    private static OrdersParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, HashMap<String, String>> summarizedData;
    private Dictionary<String, Integer> headerMap;

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
        addDefaultValuesForCurrencies(currencyIndex, data);

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

    /**
     * Adds default values for the specified currency if they do not already exist.
     * This method ensures that the "totalIncome" and "totalExpenses" keys are present
     * in the summarized data for the given currency, initializing them to "0.00" if they are absent.
     *
     * @param currency the currency for which to add default values
     */
    private void addDefaultValuesForCurrencies(int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        if (!this.summarizedData.containsKey(currency)) {
            HashMap<String, String> currencyData = new HashMap<>();
            currencyData.put("totalIncome", "0.00");
            currencyData.put("totalExpenses", "0.00");
            currencyData.put("totalProfit", "0.00");
            this.summarizedData.put(currency, currencyData);
        }
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
        updateTotalIncome(totalIndex, currencyIndex, data);
        updateTotalProfit(currencyIndex, data);
    }

    /**
     * Updates the total income based on a sell order.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalIncome(int totalIndex, int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        String amount = data[totalIndex];

        if (this.summarizedData.containsKey(currency)) {
            double currentTotal = Double.parseDouble(this.summarizedData.get(currency).get("totalIncome"));
            currentTotal += Double.parseDouble(amount);
            this.summarizedData.get(currency).put("totalIncome", formatNumberValue(String.valueOf(currentTotal), currency));
        } else {
            HashMap<String, String> currencyData = new HashMap<>();
            currencyData.put("totalIncome", formatNumberValue(amount, currency));
            this.summarizedData.put(currency, currencyData);
        }
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
        updateTotalExpenses(totalIndex, currencyIndex, data);
        updateTotalProfit(currencyIndex, data);
    }

    /**
     * Updates the total expenses based on a buy order.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalExpenses(int totalIndex, int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        String amount = data[totalIndex];

        if (this.summarizedData.containsKey(currency)) {
            double currentTotal = Double.parseDouble(this.summarizedData.get(currency).get("totalExpenses"));
            currentTotal += Double.parseDouble(amount);
            this.summarizedData.get(currency).put("totalExpenses", formatNumberValue(String.valueOf(currentTotal), currency));
        } else {
            HashMap<String, String> currencyData = new HashMap<>();
            currencyData.put("totalExpenses", formatNumberValue(amount, currency));
            this.summarizedData.put(currency, currencyData);
        }
    }

    /**
     * Updates the total profit based on current income and expenses.
     */
    private void updateTotalProfit(int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        
        Double totalIncome = Double.parseDouble(this.summarizedData.get(currency).get("totalIncome"));
        Double totalExpenses = Double.parseDouble(this.summarizedData.get(currency).get("totalExpenses"));
        this.summarizedData.get(currency).put("totalProfit", formatNumberValue(String.valueOf(totalIncome - totalExpenses), currency));
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
    public HashMap<String, HashMap<String, String>> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all stored order data and resets totals.
     */
    @Override
    public void clearData() {
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
