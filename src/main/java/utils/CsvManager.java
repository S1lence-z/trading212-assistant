package utils;

import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import app.models.ActionType;
import utils.parsers.*;

/**
 * Utility class for managing CSV files, including downloading and parsing.
 */
public class CsvManager {
    private static final Path DOWNLOAD_LOCATION_PATH = Paths.get("src/main/java/", "data");
    private static final HashMap<String, Parser> PARSERS = new HashMap<String, Parser>() {{
        put("interest", InterestParser.getInstance());
        put("transactions", TransactionsParser.getInstance());
        put("orders", OrdersParser.getInstance());
        put("dividends", DividendsParser.getInstance());
    }};

    /**
     * Gets the file path for the specified file ID.
     *
     * @param fileId The ID of the file.
     * @return The path of the CSV file.
     */
    private static Path getFilePath(String fileId) {
        return DOWNLOAD_LOCATION_PATH.resolve(fileId + ".csv");
    }

    /**
     * Parses the header of the CSV file and returns a mapping of header names to their corresponding indices.
     *
     * @param scanner The scanner used to read the CSV file.
     * @return A dictionary mapping header names to their indices.
     */
    private static Dictionary<String, Integer> parseCsvHeader(Scanner scanner) {
        String headerLine = scanner.nextLine();
        String[] splitHeader = headerLine.split(",");
        Dictionary<String, Integer> headerWordByIndex = new Hashtable<String, Integer>();

        for (int i = 0; i < splitHeader.length; i++) {
            headerWordByIndex.put(splitHeader[i], i);
        }

        return headerWordByIndex;
    }

    /**
     * Sets the header map for all parsers based on the provided header map.
     *
     * @param headerMap The dictionary mapping header names to their indices.
     */
    private static void setHeaderMapForParsers(Dictionary<String, Integer> headerMap) {
        for (Parser parser : PARSERS.values()) {
            parser.setHeaderMap(headerMap);
        }
    }

    /**
     * Downloads a CSV file from the specified link and saves it with the given file ID.
     *
     * @param downloadLink The URL from which to download the CSV file.
     * @param fileId      The ID to be used for naming the downloaded file.
     * @throws Exception if an error occurs during the download.
     */
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
    
    /**
     * Checks if a file has been downloaded with the specified file ID.
     *
     * @param fileId The ID of the file to check.
     * @return true if the file exists, false otherwise.
     */
    public static boolean isFileDownloaded(String fileId) {
        Path destination = getFilePath(fileId);
        return Files.exists(destination);
    }

    /**
     * Parses the specified CSV file and extracts its contents.
     *
     * @param fileId The ID of the file to parse.
     * @throws IOException if an error occurs while reading the file.
     */
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
                default -> throw new IllegalArgumentException("Invalid action type found in csv.");
            }
        }
        scanner.close();
    }

    /**
     * Clears the data in all parsers.
     */
    private static void clearDataInParsers() {
        for (Parser parser : PARSERS.values()) {
            parser.clearData();
        }
    }
}
