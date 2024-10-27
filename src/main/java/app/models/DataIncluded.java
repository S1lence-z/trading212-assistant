package app.models;

import com.google.gson.JsonObject;

/**
 * Represents the data inclusion options for different financial data types.
 * This class allows you to specify whether to include dividends, interest, orders, or transactions.
 */
public final class DataIncluded {
    
    /** Whether dividends are included. */
    private boolean dividends;
    
    /** Whether interest is included. */
    private boolean interest;
    
    /** Whether orders are included. */
    private boolean orders;
    
    /** Whether transactions are included. */
    private boolean transactions;

    /**
     * Constructs a new DataIncluded object with specified inclusion options.
     *
     * @param dividends Whether to include dividends.
     * @param interest Whether to include interest.
     * @param orders Whether to include orders.
     * @param transactions Whether to include transactions.
     */
    public DataIncluded(boolean dividends, boolean interest, boolean orders, boolean transactions) {
        this.dividends = dividends;
        this.interest = interest;
        this.orders = orders;
        this.transactions = transactions;
    }

    /**
     * Checks if dividends are included.
     *
     * @return true if dividends are included, false otherwise.
     */
    public boolean hasDividends() {
        return dividends;
    }

    /**
     * Checks if interest is included.
     *
     * @return true if interest is included, false otherwise.
     */
    public boolean hasInterest() {
        return interest;
    }

    /**
     * Checks if orders are included.
     *
     * @return true if orders are included, false otherwise.
     */
    public boolean hasOrders() {
        return orders;
    }

    /**
     * Checks if transactions are included.
     *
     * @return true if transactions are included, false otherwise.
     */
    public boolean hasTransactions() {
        return transactions;
    }

    /**
     * Converts a boolean value to "Yes" or "No" string.
     *
     * @param bool The boolean value to convert.
     * @return "Yes" if the boolean is true, otherwise "No".
     */
    private String writeBoolToString(boolean bool) {
        return bool ? "Yes" : "No";
    }

    /**
     * Returns a string representation of the DataIncluded object, showing
     * whether each data type is included.
     *
     * @return A formatted string representation of the DataIncluded object.
     */
    @Override
    public String toString() {
        return "Dividends: " + writeBoolToString(dividends) + "\nInterest: " + writeBoolToString(interest) + "\nOrders: " + writeBoolToString(orders) + "\nTransactions: " + writeBoolToString(transactions);
    }

    /**
     * Creates a DataIncluded object from a JSON object.
     * The JSON object should contain boolean values for the fields:
     * "includeDividends", "includeInterest", "includeOrders", "includeTransactions".
     *
     * @param dataIncluded The JSON object containing data inclusion options.
     * @return A DataIncluded object populated with values from the JSON object.
     */
    public static DataIncluded fromJsonObject(JsonObject dataIncluded) {
        return new DataIncluded(
                dataIncluded.get("includeDividends").getAsBoolean(),
                dataIncluded.get("includeInterest").getAsBoolean(),
                dataIncluded.get("includeOrders").getAsBoolean(),
                dataIncluded.get("includeTransactions").getAsBoolean()
        );
    }
}
