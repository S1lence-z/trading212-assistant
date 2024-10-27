package api;

import java.time.LocalDate;

/**
 * The TradingRequestFormatter class provides utility methods to format request bodies
 * for trading-related API calls.
 * 
 * <p>This class includes methods to generate JSON-formatted strings for POST requests
 * to export historical trading data, including transactions, orders, dividends, and interest.</p>
 * 
 * <h2>Methods:</h2>
 * <ul>
 *   <li>{@link #getBodyForPostExportHistory(LocalDate, LocalDate, boolean, boolean, boolean, boolean)}:
 *       Generates the body for a POST request to export history data.</li>
 *   <li>{@link #formatDateToISO8601(LocalDate)}:
 *       Formats a LocalDate to an ISO 8601 string.</li>
 * </ul>
 */
public class TradingRequestFormatter {
    /**
     * Generates the body for a POST request to export history data.
     *
     * @param fromDate      The start date for the export period (inclusive).
     * @param toDate        The end date for the export period (inclusive).
     * @param transactions  Whether to include transactions in the export.
     * @param orders        Whether to include orders in the export.
     * @param dividends     Whether to include dividends in the export.
     * @param interest      Whether to include interest in the export.
     * @return              A JSON-formatted string representing the request body.
     */
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
        , " \"timeFrom\": \"" + formatDateToISO8601(fromDate) + "\","
        , " \"timeTo\": \"" + formatDateToISO8601(toDate) + "\""
        , "}"
        );
        return requestBody;
    }

    /**
     * Formats a given LocalDate to an ISO 8601 date string.
     * The resulting string will be in the format "yyyy-MM-dd'T'HH:mm:ss'Z'".
     *
     * @param date The LocalDate to be formatted.
     * @return A string representing the date in ISO 8601 format.
     */
    private static String formatDateToISO8601(LocalDate date) {
        return date + "T00:00:00Z";
    }
}
