package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing interest data from CSV lines.
 * This class tracks total interest accrued and organizes the data for further processing.
 */
public class InterestParser extends Parser<String> {
    private static InterestParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Dictionary<String, Integer> headerMap;

    /**
     * Private constructor to initialize the data structures for storing interest data.
     */
    private InterestParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the InterestParser.
     *
     * @return the single instance of InterestParser.
     */
    public static synchronized InterestParser getInstance() {
        if (instance == null) {
            instance = new InterestParser();
        }
        return instance;
    }

    /**
     * Parses a line of interest data from the CSV file.
     *
     * @param line a String representing a line from the CSV file.
     * @throws RuntimeException if the header map is not set.
     */
    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for InterestParser");
        }
        int nameIndex = this.headerMap.get("Notes");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");
        handleInterestData(nameIndex, totalIndex, currencyIndex, data);
        updateTotalInterest(totalIndex, currencyIndex, data);
    }

    /**
     * Handles the organization of interest data for storage.
     *
     * @param nameIndex     the index of the notes in the CSV data.
     * @param totalIndex    the index of the total in the CSV data.
     * @param currencyIndex the index of the currency in the CSV data.
     * @param data          the parsed line data as an array of Strings.
     */
    private void handleInterestData(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
    }

    /**
     * Updates the total interest based on the parsed line data.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param currencyIndex the index of the currency in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalInterest(int totalIndex, int currencyIndex, String[] data) {
        String currency = data[currencyIndex];
        Double currencyValue = Double.parseDouble(data[totalIndex]);
        Double newValue = this.summarizedData.containsKey(currency) 
                ? Double.parseDouble(this.summarizedData.get(currency)) + currencyValue 
                : currencyValue;
        this.summarizedData.put(currency, formatNumberValue(String.valueOf(newValue), currency));
    }

    /**
     * Retrieves all parsed interest data.
     *
     * @return a HashMap containing all parsed interest data.
     */
    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    /**
     * Retrieves summarized interest data.
     *
     * @return a HashMap containing summarized interest data.
     */
    @Override
    public HashMap<String, String> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all stored interest data and resets totals.
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
