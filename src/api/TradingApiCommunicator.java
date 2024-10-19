package api;

import java.net.*;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;
import utils.KeySaver;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TradingApiCommunicator {
    private static final String API_URL = "https://live.trading212.com/api/v0";
    private static final String HISTORY_EXPORTS = "/history/exports";
    private static final HttpClient client = HttpClient.newBuilder().build();

    public static CompletableFuture<JsonArray> getExportHistoryAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonArray response = getHistoryExports();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static JsonArray getHistoryExports() throws Exception {
        var request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(API_URL + HISTORY_EXPORTS))
        .header("Content-Type", "application/json")
        .header("Authorization", KeySaver.getInstance().getApiKey())
        .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body()).getAsJsonArray();
    }

    public static CompletableFuture<JsonObject> postExportHistoryAsync(String requestBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject response = postExportHistory(requestBody);
                System.out.println(response);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

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
