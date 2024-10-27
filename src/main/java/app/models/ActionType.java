package app.models;

/**
 * Enum representing different types of actions within the application.
 * Each action type has a display name and can be identified from a string input.
 */
public enum ActionType {
    
    /** Represents an interest action. */
    INTEREST("Interest"),
    
    /** Represents a transaction action, such as deposits or withdrawals. */
    TRANSACTION("Transaction"),
    
    /** Represents a dividend action. */
    DIVIDEND("Dividend"),
    
    /** Represents an order action, such as buying or selling. */
    ORDER("Order");

    /** Display name for the action type. */
    private final String displayName;

    /**
     * Constructs an ActionType with a specified display name.
     *
     * @param displayName The display name for the action type.
     */
    ActionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the action type.
     *
     * @return The display name of the action type.
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Parses a string to determine the corresponding ActionType.
     *
     * @param actionType The string to parse.
     * @return The corresponding ActionType.
     * @throws IllegalArgumentException if the string does not match any ActionType.
     */
    public static ActionType fromString(String actionType) {
        if (isInterest(actionType)) {
            return INTEREST;
        } else if (isTransaction(actionType)) {
            return TRANSACTION;
        } else if (isOrder(actionType)) {
            return ORDER;
        } else if (isDividend(actionType)) {
            return DIVIDEND;
        }
        throw new IllegalArgumentException("No enum constant " + ActionType.class.getCanonicalName() + actionType);
    }

    /**
     * Checks if the given string represents an interest action.
     *
     * @param actionType The string to check.
     * @return true if the string matches an interest action; false otherwise.
     */
    private static boolean isInterest(String actionType) {
        return actionType.toLowerCase().contains(INTEREST.toString().toLowerCase());
    }

    /**
     * Checks if the given string represents a transaction action.
     *
     * @param actionType The string to check.
     * @return true if the string matches a transaction action; false otherwise.
     */
    private static boolean isTransaction(String actionType) {
        if (actionType.toLowerCase().contains("deposit") 
            || actionType.toLowerCase().contains("withdraw") 
            || actionType.toLowerCase().contains("currency conversion")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the given string represents an order action.
     *
     * @param actionType The string to check.
     * @return true if the string matches an order action; false otherwise.
     */
    private static boolean isOrder(String actionType) {
        if (actionType.toLowerCase().contains("buy") || actionType.toLowerCase().contains("sell")) {
            return true;
        }
        return actionType.toLowerCase().contains("market");
    }

    /**
     * Checks if the given string represents a dividend action.
     *
     * @param actionType The string to check.
     * @return true if the string matches a dividend action; false otherwise.
     */
    private static boolean isDividend(String actionType) {
        return actionType.toLowerCase().contains(DIVIDEND.toString().toLowerCase());
    }
}
