package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Abstract base class for parsing various types of data from CSV lines.
 * This class defines the essential methods that concrete parser implementations must provide.
 */
public abstract class Parser {
    protected static final String delimiter = " ---> ";

    /**
     * Parses a line of data.
     *
     * @param line a String representing a line from the CSV file.
     */
    public abstract void parse(String line);

    /**
     * Clears all parsed data.
     * This method is typically called before starting a new parsing session.
     */
    public abstract void clearData();

    /**
     * Sets the header mapping for the parser.
     * This mapping allows the parser to correctly identify columns in the CSV.
     *
     * @param headerMap a Dictionary mapping header names to their indices.
     */
    public abstract void setHeaderMap(Dictionary<String, Integer> headerMap);

    /**
     * Retrieves all parsed data.
     *
     * @return a HashMap containing all parsed data.
     */
    public abstract HashMap<String, String> getAllData();

    /**
     * Retrieves summarized data.
     *
     * @return a HashMap containing summarized data.
     */
    public abstract HashMap<String, String> getSummarizedData();

    /**
     * Formats a possible number string to a standardized decimal format.
     *
     * @param possibleNumber a String that represents a number.
     * @return a formatted String representation of the number.
     * @throws RuntimeException if the string cannot be parsed as a number.
     */
    protected String formatNumberValue(String possibleNumber) {
        try {
            double number = Double.parseDouble(possibleNumber);
            return String.format("%,.2f", number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format", e.getCause());
        }
    }
}
