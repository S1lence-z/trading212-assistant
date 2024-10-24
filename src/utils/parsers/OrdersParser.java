package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class OrdersParser extends Parser {
    private static OrdersParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalIncome = 0.0;
    private Double totalExpenses = 0.0;
    private Double totalProfit = 0.0;
    private Dictionary<String, Integer> headerMap;

    private OrdersParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
    }

    public static synchronized OrdersParser getInstance() {
        if (instance == null) {
            instance = new OrdersParser();
        }
        return instance;
    }

    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for OrdersParser");
        }
        int actionIndex = this.headerMap.get("Action");
        int nameIndex = this.headerMap.get("Name"); 
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");

        if (line.contains("buy")) {
            handleBuyOrder(actionIndex, nameIndex, totalIndex, currencyIndex, data);
            return;
        } 
        if (line.contains("sell")) {
            handleSellOrder(actionIndex, nameIndex, totalIndex, currencyIndex, data);
            return;
        }
        throw new RuntimeException("Invalid action type in OrdersParser");
    }

    //! Sell order logic
    private void handleSellOrder(int actionIndex, int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[actionIndex] + data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalIncome(totalIndex, data);
        updateTotalProfit();
    }

    private void updateTotalIncome(int totalIndex, String[] data) {
        this.totalIncome += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalIncome", formatNumberValue(String.valueOf(this.totalIncome)));
        updateTotalProfit();
    }

    //! Buy order logic
    private void handleBuyOrder(int actionIndex, int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[actionIndex] + "" + data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalExpenses(totalIndex, data);
    }

    private void updateTotalExpenses(int totalIndex, String[] data) {
        //? Calculate the total amount of expenses
        this.totalExpenses += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalExpenses", formatNumberValue(String.valueOf(this.totalExpenses)));
        updateTotalProfit();
    }

    private void updateTotalProfit() {
        //? Calculate the total profit
        this.totalProfit = this.totalIncome - this.totalExpenses;
        this.summarizedData.put("totalProfit", formatNumberValue(String.valueOf(this.totalProfit)));
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
        this.totalIncome = 0.0;
        this.totalExpenses = 0.0;
        this.totalProfit = 0.0;
        this.allData.clear();
        this.summarizedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
