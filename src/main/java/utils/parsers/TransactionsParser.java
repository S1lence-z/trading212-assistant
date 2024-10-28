package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing transaction data from CSV lines.
 * It processes deposits and withdrawals, maintains totals, and summarizes the transaction data.
 */
public class TransactionsParser extends Parser<HashMap<String, String>> {
    private static TransactionsParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, HashMap<String, String>> summarizedData;
    private Dictionary<String, Integer> headerMap;

    private TransactionsParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of TransactionsParser.
     *
     * @return the instance of TransactionsParser.
     */
    public static synchronized TransactionsParser getInstance() {
        if (instance == null) {
            instance = new TransactionsParser();
        }
        return instance;
    }

    /**
     * Parses a line of transaction data.
     * The line is expected to contain deposit or withdrawal information.
     *
     * @param line a String representing a line from the CSV file.
     * @throws RuntimeException if the header map has not been set.
     */
    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for TransactionsParser");
        }
        int nameIndex = this.headerMap.get("Notes");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");

        if (line.contains("Deposit")) {
            handleDeposit(nameIndex, totalIndex, currencyIndex, data);
        } else if (line.contains("Withdraw")) {
            handleWithdrawal(nameIndex, totalIndex, currencyIndex, data);
        }
    }

    /**
     * Handles the processing of deposit transactions.
     * It extracts relevant information from the line and updates totals.
     *
     * @param nameIndex      the index of the "Notes" column in the CSV.
     * @param totalIndex     the index of the "Total" column in the CSV.
     * @param currencyIndex  the index of the "Currency (Total)" column in the CSV.
     * @param data           an array of Strings representing the split line data.
     */
    private void handleDeposit(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalDeposits(totalIndex, currencyIndex, data);
    }

    /**
     * Updates the total deposits based on the provided data.
     *
     * @param totalIndex the index of the "Total" column in the CSV.
     * @param data      an array of Strings representing the split line data.
     */
    private void updateTotalDeposits(int totalIndex, int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        String amount = data[totalIndex];

        if (this.summarizedData.containsKey(currency)) {
            Double currentTotal = Double.parseDouble(this.summarizedData.get(currency).get("totalDeposits"));
            Double newTotal = currentTotal + Double.parseDouble(amount);
            this.summarizedData.get(currency).put("totalDeposits", formatNumberValue(String.valueOf(newTotal), currency));
        } else {
            HashMap<String, String> currencyData = new HashMap<>();
            currencyData.put("totalDeposits", formatNumberValue(amount, currency));
            this.summarizedData.put(currency, currencyData);
        }
    }

    /**
     * Handles the processing of withdrawal transactions.
     * It extracts relevant information from the line and updates totals.
     *
     * @param nameIndex      the index of the "Notes" column in the CSV.
     * @param totalIndex     the index of the "Total" column in the CSV.
     * @param currencyIndex  the index of the "Currency (Total)" column in the CSV.
     * @param data           an array of Strings representing the split line data.
     */
    private void handleWithdrawal(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalWithdrawals(totalIndex, currencyIndex, data);
    }

    /**
     * Updates the total withdrawals based on the provided data.
     *
     * @param totalIndex the index of the "Total" column in the CSV.
     * @param data      an array of Strings representing the split line data.
     */
    private void updateTotalWithdrawals(int totalIndex, int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        String amount = data[totalIndex];

        if (this.summarizedData.containsKey(currency)) {
            Double currentTotal = Double.parseDouble(this.summarizedData.get(currency).get("totalWithdrawals"));
            Double newTotal = currentTotal + Double.parseDouble(amount);
            this.summarizedData.get(currency).put("totalWithdrawals", formatNumberValue(String.valueOf(newTotal), currency));
        } else {
            HashMap<String, String> currencyData = new HashMap<>();
            currencyData.put("totalWithdrawals", formatNumberValue(amount, currency));
            this.summarizedData.put(currency, currencyData);
        }
    }

    /**
     * Returns all parsed transaction data.
     *
     * @return a HashMap containing all transaction data.
     */
    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    /**
     * Returns summarized transaction data.
     *
     * @return a HashMap containing summarized transaction data (total deposits and withdrawals).
     */
    @Override
    public HashMap<String, HashMap<String, String>> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all parsed data and resets totals.
     * This method is typically called before a new parsing session begins.
     */
    @Override
    public void clearData() {
        this.allData.clear();
        this.summarizedData.clear();
    }

    /**
     * Sets the header mapping for the parser.
     * This mapping allows the parser to correctly identify columns in the CSV.
     *
     * @param headerMap a Dictionary mapping header names to their indices.
     */
    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
