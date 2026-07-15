package utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JsonReader {

    private JsonReader() {
    }

    public static JSONObject read(String resourcePath) {
        try (InputStream inputStream = JsonReader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalStateException(resourcePath + " could not be found");
            }

            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return new JSONObject(content);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + resourcePath, e);
        }
    }
}
