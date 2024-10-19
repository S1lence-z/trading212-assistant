package api;

import java.time.LocalDate;

public class TradingRequestFormatter {
    public static String getBodyForPostExportHistory(
        LocalDate fromDate, 
        LocalDate toDate, 
        boolean transactions, 
        boolean orders, 
        boolean dividends, 
        boolean interest) {
        String requestBody = String.join("\n"
        , "{"
        , " \"dataIncluded\": {"
        , "  \"includeDividends\": " + dividends + ","
        , "  \"includeInterest\": " + interest + ","
        , "  \"includeOrders\": " + orders + ","
        , "  \"includeTransactions\": " + transactions
        , " },"
        , " \"timeFrom\": \"" + TradingRequestFormatter.formatDateToISO8601(fromDate) + "\","
        , " \"timeTo\": \"" + TradingRequestFormatter.formatDateToISO8601(toDate) + "\""
        , "}"
        );
        return requestBody;
    }

    private static String formatDateToISO8601(LocalDate date) {
        return date + "T00:00:00Z";
    }
}
