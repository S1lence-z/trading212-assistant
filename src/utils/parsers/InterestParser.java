package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class InterestParser extends Parser {
    private static InterestParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalInterest = 0.0;
    private Dictionary<String, Integer> headerMap;

    private InterestParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
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
        handleInterestData(nameIndex, totalIndex, currencyIndex, data);
        updateTotalInterest(totalIndex, data);
    }

    private void handleInterestData(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
    }

    private void updateTotalInterest(int totalIndex, String[] data) {
        this.totalInterest += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalInterest", formatNumberValue(String.valueOf(this.totalInterest)));
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
    public void clearParsedData() {
        this.totalInterest = 0.0;
        this.allData.clear();
        this.summarizedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}