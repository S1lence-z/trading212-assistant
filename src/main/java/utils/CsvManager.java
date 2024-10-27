package utils;

import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import app.models.ActionType;
import utils.parsers.*;

public class CsvManager {
    private static final Path DOWNLOAD_LOCATION_PATH = Paths.get("src/main/java/", "data");
    private static final HashMap<String, Parser> PARSERS = new HashMap<String, Parser>() {{
        put("interest", InterestParser.getInstance());
        put("transactions", TransactionsParser.getInstance());
        put("orders", OrdersParser.getInstance());
        put("dividends", DividendsParser.getInstance());
    }};

    private static Path getFilePath(String fileId) {
        return DOWNLOAD_LOCATION_PATH.resolve(fileId + ".csv");
    }

    private static Dictionary<String, Integer> parseCsvHeader(Scanner scanner) {
        String headerLine = scanner.nextLine();
        String[] splitHeader = headerLine.split(",");
        Dictionary<String, Integer> headerWordByIndex = new Hashtable<String, Integer>();

        for (int i = 0; i < splitHeader.length; i++) {
            headerWordByIndex.put(splitHeader[i], i);
        }

        return headerWordByIndex;
    }

    private static void setHeaderMapForParsers(Dictionary<String, Integer> headerMap) {
        for (Parser parser : PARSERS.values()) {
            parser.setHeaderMap(headerMap);
        }
    }

    public static void downloadCsvFile(String downloadLink, String fileId) throws Exception {
        try {
            if (!Files.exists(DOWNLOAD_LOCATION_PATH)) {
                Files.createDirectories(DOWNLOAD_LOCATION_PATH);
            }
            Path destination = getFilePath(fileId);
            URL url = new URI(downloadLink).toURL();
            Files.copy(url.openStream(), destination);
            System.out.println("Downloaded CSV file successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static boolean isFileDownloaded(String fileId) {
        Path destination = getFilePath(fileId);
        return Files.exists(destination);
    }

    public static void parseCsvFile(String fileId) throws IOException {
        clearDataInParsers();
        Path destination = getFilePath(fileId);
        Scanner scanner = new Scanner(destination);
        Dictionary<String, Integer> headerMap = parseCsvHeader(scanner);
        setHeaderMapForParsers(headerMap);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] splitLine = line.split(",");
            String actionTypeString = splitLine[headerMap.get("Action")];
            ActionType actionType = ActionType.fromString(actionTypeString);

            switch (actionType) {
                case INTEREST -> PARSERS.get("interest").parse(line);
                case TRANSACTION -> PARSERS.get("transactions").parse(line);
                case ORDER -> PARSERS.get("orders").parse(line);
                case DIVIDEND -> PARSERS.get("dividends").parse(line);
                default
                    -> 
                    throw new IllegalArgumentException("Invalid action type found in csv.");
            }
        }
        scanner.close();
    }

    private static void clearDataInParsers() {
        for (Parser parser : PARSERS.values()) {
            parser.clearData();
        }
    }
}
