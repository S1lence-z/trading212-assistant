package utils.parsers;

import java.util.Dictionary;
import java.util.HashMap;

public class TransactionsParser extends Parser {
    private static TransactionsParser instance = null;
    private HashMap<String, String> allData;
    private HashMap<String, String> summarizedData;
    private Double totalDeposits = 0.0;
    private Double totalWithdrawals = 0.0;
    private Dictionary<String, Integer> headerMap;

    private TransactionsParser() {
        this.allData = new HashMap<>();
        this.summarizedData = new HashMap<>();
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
            throw new RuntimeException("Header map not set for TransactionsParser");
        }
        int nameIndex = this.headerMap.get("Notes");
        int totalIndex = this.headerMap.get("Total");
        int currencyIndex = this.headerMap.get("Currency (Total)");
        String[] data = line.split(",");

        if (line.contains("Deposit")) {
            handleDeposit(nameIndex, totalIndex, currencyIndex, data);
        }
        else if (line.contains("Withdraw")) {
            handleWithdrawal(nameIndex, totalIndex, currencyIndex, data);
        }
    }

    private void handleDeposit(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalDeposits(totalIndex, data);
    }

    private void updateTotalDeposits(int totalIndex, String[] data) {
        // Calculate the total amount of deposits
        this.totalDeposits += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalDeposits", formatNumberValue(String.valueOf(this.totalDeposits)));
    }

    private void handleWithdrawal(int nameIndex, int totalIndex, int currencyIndex, String[] data) {
        String value = data[nameIndex] + " " + data[totalIndex] + " " + data[currencyIndex];
        int lineNumber = this.allData.size() + 1;
        this.allData.put(String.valueOf(lineNumber), value);
        updateTotalWithdrawals(totalIndex, data);
    }

    private void updateTotalWithdrawals(int totalIndex, String[] data) {
        // Calculate the total amount of withdrawals
        this.totalWithdrawals += Double.parseDouble(data[totalIndex]);
        this.summarizedData.put("totalWithdrawals", formatNumberValue(String.valueOf(this.totalWithdrawals)));
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
        this.allData.clear();
        this.summarizedData.clear();
        this.totalDeposits = 0.0;
        this.totalWithdrawals = 0.0;
    }

    @Override
    public void setHeaderMap(Dictionary<String, Integer> headerMap) {
        this.headerMap = headerMap;
    }
}
