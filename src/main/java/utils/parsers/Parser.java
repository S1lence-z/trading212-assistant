package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Abstract base class for parsing various types of data from CSV lines.
 * Concrete implementations must define the methods for parsing, clearing, and summarizing data.
 *
 * @param <T> the type of summarized data specific to each parser implementation.
 */
public abstract class Parser<T> {
    protected static final String delimiter = " ---> ";

    /**
     * Parses a line of data from the CSV file.
     * This method is called for each line of the CSV data and is responsible for processing and storing relevant information.
     *
     * @param line a String representing a single line from the CSV file.
     */
    public abstract void parse(String line);

    /**
     * Clears all parsed data.
     * Resets the internal data structures, preparing the parser for a fresh parsing session.
     */
    public abstract void clearData();

    /**
     * Sets the header mapping to identify and access column indices in the CSV.
     * The header mapping is used in parsing methods to correctly extract data from specific columns.
     *
     * @param headerMap a Dictionary mapping header names to their corresponding column indices.
     */
    public abstract void setHeaderMap(Dictionary<String, Integer> headerMap);

    /**
     * Retrieves all parsed data in a map format.
     * Each entry represents an individual line parsed from the CSV and its associated data.
     *
     * @return a HashMap containing all parsed data from the CSV, organized by line identifiers.
     */
    public abstract HashMap<String, String> getAllData();

    /**
     * Retrieves summarized data, typically aggregating key metrics across all parsed entries.
     * This method is implemented differently in each concrete parser to provide relevant summary data.
     *
     * @return a HashMap containing summarized data specific to each parser's functionality.
     */
    public abstract HashMap<String, T> getSummarizedData();

    /**
     * Formats a number represented as a string into a standardized decimal format.
     * This method ensures consistent decimal formatting, typically to two decimal places,
     * for currency values or other numeric data.
     *
     * @param possibleNumber a String representing a number, which may be parsed into a decimal format.
     * @param currencyCode a String representing the currency code, for clarity in format standardization.
     * @return a formatted String representation of the number, rounded to two decimal places.
     * @throws RuntimeException if the string cannot be parsed as a valid number.
     */
    protected String formatNumberValue(String possibleNumber, String currencyCode) {
        try {
            double number = Double.parseDouble(possibleNumber);
            return String.format(java.util.Locale.US, "%.2f", number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format", e.getCause());
        }
    }
}
