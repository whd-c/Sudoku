import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class CacheManager {

    private final String fileName;
    private final Gson gson;

    public CacheManager(String _fileName) {
        fileName = _fileName;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void write(CacheData data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Failed to write cache: " + e.getMessage());
        }
    }

    public CacheData read() {
        try (FileReader reader = new FileReader(fileName)) {
            CacheData data = gson.fromJson(reader, CacheData.class);
            if (data != null) {
                return data;
            }
        } catch (IOException e) {
            System.out.println("Failed to read cache: " + e.getMessage());
        }

        return new CacheData("EASY", 0);
    }

    public void clear() {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
    }
}