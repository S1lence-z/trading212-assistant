package app.models;

public enum ActionType {
    INTEREST("Interest"),
    TRANSACTION("Transaction"),
    DIVIDEND("Dividend"),
    ORDER("Order"),;

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

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

    private static boolean isInterest(String actionType) {
        return actionType.toLowerCase().contains(INTEREST.toString().toLowerCase());
    }

    private static boolean isTransaction(String actionType) {
        if (actionType.toLowerCase().contains("deposit") 
            || actionType.toLowerCase().contains("withdraw") 
            || actionType.toLowerCase().contains("currency conversion")) {
            return true;
        }
        return false;
    }

    private static boolean isOrder(String actionType) {
        if (actionType.toLowerCase().contains("buy") || actionType.toLowerCase().contains("sell")) {
            return true;
        }
        return actionType.toLowerCase().contains("market");
    }

    private static boolean isDividend(String actionType) {
        return actionType.toLowerCase().contains(DIVIDEND.toString().toLowerCase());
    }
}
