package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class InterestParser extends Parser {
    private static InterestParser instance = null;
    private HashMap<String, String> parsedData;
    private Double totalInterest = 0.0;
    private Dictionary<String, Integer> headerMap;

    private InterestParser() {
        this.parsedData = new HashMap<>();
    }

    public static synchronized InterestParser getInstance() {
        if (instance == null) {
            instance = new InterestParser();
        }
        return instance;
    }

    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for InterestParser");
        }
        int nameIndex = this.headerMap.get("Notes");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");
        String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.parsedData.size() + 1;
        this.parsedData.put(String.valueOf(lineNumber), value);
        // Calculate the total amount of interest
        this.totalInterest += Double.parseDouble(data[totalIndex]);
        this.parsedData.put("totalInterest", String.valueOf(this.totalInterest));
    }

    @Override
    public HashMap<String, String> getParsedData() {
        return this.parsedData;
    }

    @Override
    public void clearParsedData() {
        this.totalInterest = 0.0;
        this.parsedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}