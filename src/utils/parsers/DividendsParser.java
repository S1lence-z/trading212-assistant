package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class DividendsParser extends Parser {
    private static DividendsParser instance = null;
    private HashMap<String, String> parsedData;
    private Double totalDividends = 0.0;
    private Dictionary<String, Integer> headerMap;

    private DividendsParser() {
        this.parsedData = new HashMap<String, String>();
    }

    public static synchronized DividendsParser getInstance() {
        if (instance == null) {
            instance = new DividendsParser();
        }
        return instance;
    }

    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for DividendsParser");
        }
        int nameIndex = this.headerMap.get("Name");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");        
        String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.parsedData.size() + 1;
        this.parsedData.put(String.valueOf(lineNumber), value);
        // Calculate the total amount of dividends
        this.totalDividends += Double.parseDouble(data[totalIndex]);
        this.parsedData.put("totalDividends", String.valueOf(this.totalDividends));
    }

    @Override
    public HashMap<String, String> getParsedData() {
        return this.parsedData;
    }

    @Override
    public void clearParsedData() {
        this.totalDividends = 0.0;
        this.parsedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
