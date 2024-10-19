package utils;

import java.io.*;
import java.nio.file.Paths;

import javafx.concurrent.Task;

public class KeySaver {
    private static KeySaver instance = null;
    private static final String FILE_NAME = "api_key.txt";
    private static final String FILE_PATH = Paths.get("src", FILE_NAME).toString();
    private static String apiKey = "";

    private KeySaver() { }

    public static synchronized KeySaver getInstance() {
        if (instance == null) {
            instance = new KeySaver();
        }
        return instance;
    }

    public boolean setApiKey(String value) {
        if (apiKey.isEmpty()) {
            apiKey = value;
            return true;
        }
        return false;
    }

    public String getApiKey() {
        return apiKey;
    }

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
