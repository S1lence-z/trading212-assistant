package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class TransactionsParser extends Parser {
    private static TransactionsParser instance = null;
    private HashMap<String, String> parsedData;
    private Double totalDeposits = 0.0;
    private Double totalWithdrawals = 0.0;
    private Dictionary<String, Integer> headerMap;

    private TransactionsParser() {
        this.parsedData = new HashMap<>();
    }

    public static synchronized TransactionsParser getInstance() {
        if (instance == null) {
            instance = new TransactionsParser();
        }
        return instance;
    }

    @Override
    public void parse(String line) {
        if (this.headerMap == null) {
            throw new RuntimeException("Header map not set for DividendsParser");
        }
        if (line.contains("Deposit")) {
            int nameIndex = this.headerMap.get("Notes");
            int totalIndex = this.headerMap.get("Total");
            int currencyIndex = this.headerMap.get("Currency (Total)");
            String[] data = line.split(",");
            String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
            int lineNumber = this.parsedData.size() + 1;
            this.parsedData.put(String.valueOf(lineNumber), value);
            // Calculate the total amount of deposits
            this.totalDeposits += Double.parseDouble(data[totalIndex]);
            this.parsedData.put("totalDeposits", String.valueOf(this.totalDeposits));
            return;
        }
        if (line.contains("Withdraw")) {
            int nameIndex = this.headerMap.get("Notes");
            int totalIndex = this.headerMap.get("Total");
            int currencyIndex = this.headerMap.get("Currency (Total)");
            String[] data = line.split(",");
            String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
            int lineNumber = this.parsedData.size() + 1;
            this.parsedData.put(String.valueOf(lineNumber), value);
            // Calculate the total amount of withdrawals
            this.totalWithdrawals += Double.parseDouble(data[totalIndex]);
            this.parsedData.put("totalWithdrawals", String.valueOf(this.totalWithdrawals));
            return;
        }
    }

    @Override
    public HashMap<String, String> getParsedData() {
        return this.parsedData;
    }

    @Override
    public void clearParsedData() {
        this.parsedData.clear();
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
