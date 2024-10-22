package utils;

import java.net.*;
import java.nio.file.*;

public class CsvManager {
    private static final Path DOWNLOAD_LOCATION_PATH = Paths.get("src", "data");

    public static void downloadCsvFile(String downloadLink, String fileId) throws Exception {
        try {
            if (!Files.exists(DOWNLOAD_LOCATION_PATH)) {
                Files.createDirectories(DOWNLOAD_LOCATION_PATH);
            }
            Path destination = getFileDestination(fileId);
            URL url = new URI(downloadLink).toURL();
            Files.copy(url.openStream(), destination);
            System.out.println("Downloaded CSV file successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static boolean isFileDownloaded(String fileId) {
        Path destination = getFileDestination(fileId);
        return Files.exists(destination);
    }

    private static Path getFileDestination(String fileId) {
        return DOWNLOAD_LOCATION_PATH.resolve(fileId + ".csv");
    }
}
