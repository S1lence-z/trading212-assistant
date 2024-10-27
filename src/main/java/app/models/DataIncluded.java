package app.models;

import com.google.gson.JsonObject;

public final class DataIncluded {
    private boolean dividends;
    private boolean interest;
    private boolean orders;
    private boolean transactions;

    public DataIncluded(boolean dividends, boolean interest, boolean orders, boolean transactions) {
        this.dividends = dividends;
        this.interest = interest;
        this.orders = orders;
        this.transactions = transactions;
    }

    public boolean hasDividends() {
        return dividends;
    }

    public boolean hasInterest() {
        return interest;
    }

    public boolean hasOrders() {
        return orders;
    }

    public boolean hasTransactions() {
        return transactions;
    }

    private String writeBoolToString(boolean bool) {
        return bool ? "Yes" : "No";
    }

    @Override
    public String toString() {
        return "Dividends: " + writeBoolToString(dividends) + "\nInterest: " + writeBoolToString(interest) + "\nOrders: " + writeBoolToString(orders) + "\nTransactions: " + writeBoolToString(transactions);
    }

    public static DataIncluded fromJsonObject(JsonObject dataIncluded) {
        return new DataIncluded(
                dataIncluded.get("includeDividends").getAsBoolean(),
                dataIncluded.get("includeInterest").getAsBoolean(),
                dataIncluded.get("includeOrders").getAsBoolean(),
                dataIncluded.get("includeTransactions").getAsBoolean()
        );
    }
}
