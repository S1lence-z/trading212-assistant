package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing transaction data from CSV lines.
 * It processes deposit and withdrawal transactions, maintains totals, and provides summarized data.
 * This class is designed to handle and organize transaction data in a currency-specific manner.
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
     * @return the single instance of TransactionsParser.
     */
    public static synchronized TransactionsParser getInstance() {
        if (instance == null) {
            instance = new TransactionsParser();
        }
        return instance;
    }

    /**
     * Parses a line of transaction data.
     * This method handles deposit and withdrawal transactions, parsing information such as
     * total amount, currency, and additional notes. Updates to totals and summaries
     * are made accordingly based on transaction type.
     *
     * @param line a String representing a line from the CSV file.
     * @throws RuntimeException if the header map is not set.
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
     * Processes deposit transactions, extracting the necessary fields and updating totals.
     *
     * @param nameIndex     the index of the "Notes" column in the CSV.
     * @param totalIndex    the index of the "Total" column in the CSV.
     * @param currencyIndex the index of the "Currency (Total)" column in the CSV.
     * @param data          an array of Strings representing the split line data.
     */
    private void handleDeposit(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalDeposits(totalIndex, currencyIndex, data);
    }

    /**
     * Updates the total deposits for a specific currency based on transaction data.
     * This method ensures that each currency has an updated deposit total.
     *
     * @param totalIndex    the index of the "Total" column in the CSV.
     * @param currencyIndex the index of the "Currency (Total)" column in the CSV.
     * @param data          an array of Strings representing the split line data.
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
     * Processes withdrawal transactions, extracting the necessary fields and updating totals.
     *
     * @param nameIndex     the index of the "Notes" column in the CSV.
     * @param totalIndex    the index of the "Total" column in the CSV.
     * @param currencyIndex the index of the "Currency (Total)" column in the CSV.
     * @param data          an array of Strings representing the split line data.
     */
    private void handleWithdrawal(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalWithdrawals(totalIndex, currencyIndex, data);
    }

    /**
     * Updates the total withdrawals for a specific currency based on transaction data.
     * This method ensures that each currency has an updated withdrawal total.
     *
     * @param totalIndex    the index of the "Total" column in the CSV.
     * @param currencyIndex the index of the "Currency (Total)" column in the CSV.
     * @param data          an array of Strings representing the split line data.
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
     * Returns all parsed transaction data in a mapped format, preserving the order of entry.
     *
     * @return a HashMap containing all transaction data parsed from the CSV.
     */
    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    /**
     * Returns a summarized view of transaction data, organized by currency.
     * Includes total deposits and withdrawals for each currency encountered in the data.
     *
     * @return a HashMap containing summarized transaction data.
     */
    @Override
    public HashMap<String, HashMap<String, String>> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all parsed data, resetting internal structures to prepare for new data parsing.
     */
    @Override
    public void clearData() {
        this.allData.clear();
        this.summarizedData.clear();
    }

    /**
     * Sets the header mapping dictionary, which provides indices for key columns.
     * This mapping must be set before parsing to ensure correct data extraction.
     *
     * @param headerMap a Dictionary mapping header names to their indices in the CSV.
     */
    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
