package api;

import java.net.*;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;
import utils.KeySaver;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The TradingApiCommunicator class provides methods to interact with the Trading212 API.
 * It supports asynchronous operations for fetching and posting export history data.
 * 
 * <p>API URL: {@value #API_URL}</p>
 * 
 * <p>Endpoints:</p>
 * <ul>
 *   <li>{@value #HISTORY_EXPORTS} - Endpoint for export history operations</li>
 * </ul>
 * 
 * <p>Usage:</p>
 * <pre>
 * {@code
 * CompletableFuture<JsonElement> historyFuture = TradingApiCommunicator.getExportHistoryAsync();
 * historyFuture.thenAccept(history -> {
 *     // Process the history data
 * });
 * 
 * String requestBody = "{...}"; // Example request body in JSON format
 * CompletableFuture<JsonObject> postFuture = TradingApiCommunicator.postExportHistoryAsync(requestBody);
 * postFuture.thenAccept(response -> {
 *     // Process the response data
 * });
 * }
 * </pre>
 * 
 * <p>Dependencies:</p>
 * <ul>
 *   <li>java.net.http.HttpClient</li>
 *   <li>com.google.gson.JsonElement</li>
 *   <li>com.google.gson.JsonObject</li>
 *   <li>com.google.gson.JsonParser</li>
 * </ul>
 * 
 * <p>Note: Ensure that the API key is set in the KeySaver instance before making requests.</p>
 * 
 * @see java.net.http.HttpClient
 * @see com.google.gson.JsonElement
 * @see com.google.gson.JsonObject
 * @see com.google.gson.JsonParser
 */
public class TradingApiCommunicator {
    private static final String API_URL = "https://live.trading212.com/api/v0";
    private static final String HISTORY_EXPORTS = "/history/exports";
    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * Asynchronously retrieves the export history.
     *
     * This method returns a CompletableFuture that, when completed, will contain
     * a JsonElement representing the export history. The history is fetched by
     * calling the getHistoryExportsArray() method.
     *
     * @return a CompletableFuture containing the export history as a JsonElement.
     */
    public static CompletableFuture<JsonElement> getExportHistoryAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonElement response = getHistoryExportsArray();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Retrieves the history exports array from the Trading API.
     *
     * This method sends a GET request to the Trading API's history exports endpoint
     * and returns the response as a JsonElement.
     *
     * @return JsonElement representing the history exports array.
     * @throws Exception if an error occurs during the HTTP request or response parsing.
     */
    private static JsonElement getHistoryExportsArray() throws Exception {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + HISTORY_EXPORTS))
                .header("Content-Type", "application/json")
                .header("Authorization", KeySaver.getInstance().getApiKey())
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body());
    }

    /**
     * Asynchronously sends a POST request to export history with the given request body.
     *
     * @param requestBody The request body to be sent in the POST request, in JSON format.
     * @return A CompletableFuture that, when completed, will contain the JsonObject response from the POST request.
     *         If an exception occurs during the request, the CompletableFuture will complete with null.
     */
    public static CompletableFuture<JsonObject> postExportHistoryAsync(String requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = postExportHistory(requestBody);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Sends a POST request to export history data.
     *
     * @param requestBody The JSON string to be sent in the request body.
     * @return A JsonObject containing the response from the server.
     * @throws Exception If an error occurs during the request.
     */
    private static JsonObject postExportHistory(String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(API_URL + HISTORY_EXPORTS))
                .header("Content-Type", "application/json")
                .header("Authorization", KeySaver.getInstance().getApiKey())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}
