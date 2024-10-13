package utils;

import java.io.*;
import java.nio.file.Paths;

public class KeySaver {
    private static final String FILE_NAME = "api_key.txt";
    private static final String FILE_PATH = Paths.get("src", FILE_NAME).toString();

    public static void saveKey(String key) {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            writer.write(key);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getKey() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String key = reader.readLine();
            reader.close();
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteKey() {
        File file = new File(FILE_PATH);
        file.delete();
    }

    public static boolean keyExists() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            boolean keyExists = reader.readLine() != null;
            reader.close();
            return keyExists;
        } catch (IOException e) {
            return false;
        }
    }
}
