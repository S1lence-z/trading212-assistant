package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public abstract class Parser {
    protected static final Character delimiter = '_';
    
    public abstract void parse(String line);

    protected abstract void clearParsedData();

    public abstract void setHeaderMap(Dictionary<String, Integer> headerMap);

    public abstract HashMap<String, String> getAllData();

    public abstract HashMap<String, String> getSummarizedData();

    protected String formatNumberValue(String possibleNumber) {
        try {
            double number = Double.parseDouble(possibleNumber);
            return String.format("%,.2f", number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format", e.getCause());
        }
    }
}