package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class OrdersParser extends Parser {
    private static OrdersParser instance = null;
    private HashMap<String, String> parsedData;
    private Double totalIncome = 0.0;
    private Double totalExpenses = 0.0;
    private Double totalProfit = 0.0;
    private Dictionary<String, Integer> headerMap;

    private OrdersParser() {
        this.parsedData = new HashMap<>();
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
            throw new RuntimeException("Header map not set for DividendsParser");
        }
        if (line.contains("buy")) {
            int actionIndex = this.headerMap.get("Action");
            int nameIndex = this.headerMap.get("Name"); 
            int totalIndex = this.headerMap.get("Total");
            int currencyIndex = this.headerMap.get("Currency (Total)");
            String[] data = line.split(",");
            String value = data[actionIndex] + "" + data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
            int lineNumber = this.parsedData.size() + 1;
            this.parsedData.put(String.valueOf(lineNumber), value);
            // Calculate the total amount of expenses
            this.totalExpenses += Double.parseDouble(data[totalIndex]);
            this.parsedData.put("totalExpenses", String.valueOf(this.totalExpenses));
            // Calculate the total profit
            this.totalProfit = this.totalIncome - this.totalExpenses;
            this.parsedData.put("totalProfit", String.valueOf(this.totalProfit));
            return;
        } 
        if (line.contains("sell")) {
            int actionIndex = this.headerMap.get("Action");
            int nameIndex = this.headerMap.get("Name"); 
            int totalIndex = this.headerMap.get("Total");
            int currencyIndex = this.headerMap.get("Currency (Total)");
            String[] data = line.split(",");
            String value = data[actionIndex] + data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
            int lineNumber = this.parsedData.size() + 1;
            this.parsedData.put(String.valueOf(lineNumber), value);
            // Calculate the total amount of income
            this.totalIncome += Double.parseDouble(data[totalIndex]);
            this.parsedData.put("totalIncome", String.valueOf(this.totalIncome));
            // Calculate the total profit
            this.totalProfit = this.totalIncome - this.totalExpenses;
            this.parsedData.put("totalProfit", String.valueOf(this.totalProfit));
            return;
        }
        throw new RuntimeException("Invalid action type in OrdersParser");
    }

    @Override
    public HashMap<String, String> getParsedData() {
        return this.parsedData;
    }

    @Override
    public void clearParsedData() {
        this.totalIncome = 0.0;
        this.totalExpenses = 0.0;
        this.parsedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
