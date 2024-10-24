package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class DividendsParser extends Parser {
    private static DividendsParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalDividends = 0.0;
    private Dictionary<String, Integer> headerMap;

    private DividendsParser() {
        this.allData = new HashMap<String, String>();
        this.summarizedData = new HashMap<String, String>();
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
        handleDividendData(nameIndex, totalIndex, currencyIndex, data);
        updateTotalDividends(totalIndex, data);
    }

    private void handleDividendData(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex].strip() + delimiter + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
    }

    private void updateTotalDividends(int totalIndex, String[] data) {
        this.totalDividends += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalDividends", formatNumberValue(String.valueOf(this.totalDividends)));
    }

    @Override
    public HashMap<String, String> getAllData() {
        return this.allData;
    }

    @Override
    public HashMap<String, String> getSummarizedData() {
        return this.summarizedData;
    }

    @Override
    public void clearData() {
        this.totalDividends = 0.0;
        this.allData.clear();
        this.summarizedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
