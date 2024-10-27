package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Singleton class responsible for parsing dividend data from CSV lines.
 * This class tracks total dividends and organizes the data for further processing.
 */
public class DividendsParser extends Parser {
    private static DividendsParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalDividends = 0.0;
    private Dictionary<String, Integer> headerMap;

    /**
     * Retrieves the total amount of dividends.
     *
     * @return the total dividends as a Double.
     */
    public Double getTotalDividends() {
        return totalDividends;
    }

    private DividendsParser() {
        this.allData = new HashMap<String, String>();
        this.summarizedData = new HashMap<String, String>();
    }

    /**
     * Retrieves the singleton instance of the DividendsParser.
     *
     * @return the single instance of DividendsParser.
     */
    public static synchronized DividendsParser getInstance() {
        if (instance == null) {
            instance = new DividendsParser();
        }
        return instance;
    }

    /**
     * Parses a line of dividend data from the CSV file.
     *
     * @param line a String representing a line from the CSV file.
     * @throws RuntimeException if the header map is not set.
     */
    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for DividendsParser");
        }
        int nameIndex = this.headerMap.get("Name");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");        
        handleDividendData(nameIndex, totalIndex, currencyIndex, data);
        updateTotalDividends(totalIndex, data);
    }

    /**
     * Handles the organization of dividend data for storage.
     *
     * @param nameIndex     the index of the name in the CSV data.
     * @param totalIndex    the index of the total in the CSV data.
     * @param currencyIndex the index of the currency in the CSV data.
     * @param data          the parsed line data as an array of Strings.
     */
    private void handleDividendData(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex].strip() + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
    }

    /**
     * Updates the total dividends based on the parsed line data.
     *
     * @param totalIndex the index of the total in the CSV data.
     * @param data      the parsed line data as an array of Strings.
     */
    private void updateTotalDividends(int totalIndex, String[] data) {
        this.totalDividends += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalDividends", formatNumberValue(String.valueOf(this.totalDividends)));
    }

    /**
     * Retrieves all parsed dividend data.
     *
     * @return a HashMap containing all parsed dividend data.
     */
    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    /**
     * Retrieves summarized dividend data.
     *
     * @return a HashMap containing summarized dividend data.
     */
    @Override
    public HashMap<String, String> getSummarizedData() {
        return this.summarizedData;
    }

    /**
     * Clears all stored dividend data and resets totals.
     */
    @Override
    public void clearData() {
        this.totalDividends = 0.0;
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
