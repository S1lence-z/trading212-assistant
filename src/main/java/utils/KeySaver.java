package utils;

import java.io.*;
import java.nio.file.Paths;
import javafx.concurrent.Task;

/**
 * Singleton class for saving and loading an API key to and from a text file.
 */
public class KeySaver {
    private static KeySaver instance = null;
    private static final String FILE_NAME = "api_key.txt";
    private static final String FILE_PATH = Paths.get("src/main/resources/", FILE_NAME).toString();
    private static String apiKey = "";

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private KeySaver() { }

    /**
     * Retrieves the singleton instance of KeySaver.
     *
     * @return The single instance of KeySaver.
     */
    public static synchronized KeySaver getInstance() {
        if (instance == null) {
            instance = new KeySaver();
        }
        return instance;
    }

    /**
     * Sets the API key if it is currently empty.
     *
     * @param value The API key to set.
     * @return true if the key was set successfully, false if it was not (i.e., it was already set).
     */
    public boolean setApiKey(String value) {
        if (apiKey.isEmpty()) {
            apiKey = value;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the currently stored API key.
     *
     * @return The API key, or an empty string if not set.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Saves the given API key to a file asynchronously.
     *
     * @param key The API key to save.
     */
    public static void saveKeyToFileAsync(String key) {
        Task<Void> saveKeyTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                saveKeyToFile(key);
                return null;
            }
        };

        saveKeyTask.setOnSucceeded(event -> {
            System.out.println("Key saved successfully");
        });

        saveKeyTask.setOnFailed(event -> {
            event.getSource().getException().printStackTrace();
            String exceptionMessage = saveKeyTask.getException().getMessage();
            throw new RuntimeException(exceptionMessage);
        });

        new Thread(saveKeyTask).start();
    }

    /**
     * Saves the specified API key to the designated file.
     *
     * @param key The API key to save.
     */
    private static void saveKeyToFile(String key) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write(key);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Loads the API key from the file asynchronously.
     *
     * @return A Task that, when executed, will return the loaded API key.
     */
    public static Task<String> loadKeyFromFileAsync() {
        Task<String> loadKeyTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return loadKeyFromFile();
            }
        };
        new Thread(loadKeyTask).start();
        return loadKeyTask;
    }

    /**
     * Loads the API key from the designated file.
     *
     * @return The loaded API key, or an empty string if the file does not exist or is empty.
     */
    private static String loadKeyFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String key = reader.readLine();
            if (key == null) {
                reader.close();
                return "";
            }
            reader.close();
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
