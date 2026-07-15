package utils;

import org.json.JSONObject;

public class ExpectedTextReader {

    private static final JSONObject expectedTexts = JsonReader.read("testdata/expectedTexts.json");

    public static String getErrorMessage(String key) {
        return expectedTexts.getJSONObject("errorMessages").getString(key);
    }

    public static String getPageTitle(String key) {
        return expectedTexts.getJSONObject("pageTitles").getString(key);
    }

    public static String getProductTitle(int index) {
        return expectedTexts.getJSONArray("products").getJSONObject(index).getString("title");
    }

    public static String getProductPrice(int index) {
        return expectedTexts.getJSONArray("products").getJSONObject(index).getString("price");
    }

    public static int getProductCount() {
        return expectedTexts.getJSONArray("products").length();
    }

    public static String getCartItem(String key) {
        return expectedTexts.getJSONObject("cartItems").getString(key);
    }

    public static String getCheckoutComplete(String key) {
        return expectedTexts.getJSONObject("checkoutComplete").getString(key);
    }
}
