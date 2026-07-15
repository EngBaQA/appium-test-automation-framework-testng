package utils;

import org.json.JSONObject;

public class TestDataReader {

    private static final JSONObject testData = JsonReader.read("testdata/loginTestData.json");

    public static String getUsername(String scenario) {
        return testData.getJSONObject(scenario).getString("username");
    }

    public static String getPassword(String scenario) {
        return testData.getJSONObject(scenario).getString("password");
    }
}
