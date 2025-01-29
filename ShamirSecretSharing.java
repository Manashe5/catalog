import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;

public class ShamirSecretSharing {
    
    public static void main(String[] args) throws Exception {
        // Load test cases from JSON files
        String json1 = new String(Files.readAllBytes(Paths.get("testcase1.json")));
        String json2 = new String(Files.readAllBytes(Paths.get("testcase2.json")));
        
        System.out.println("Secret from Test Case 1: " + findSecret(json1));
        System.out.println("Secret from Test Case 2: " + findSecret(json2));
    }
    
    private static int findSecret(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<Integer> xValues = new ArrayList<>();
        List<Integer> yValues = new ArrayList<>();
        
        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                JSONObject entry = json.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = entry.getInt("base");
                int y = new BigInteger(entry.getString("value"), base).intValue();
                xValues.add(x);
                yValues.add(y);
            }
        }
        
        return lagrangeInterpolation(xValues, yValues, 0, k);
    }
    
    private static int lagrangeInterpolation(List<Integer> x, List<Integer> y, int x0, int k) {
        int secret = 0;
        for (int i = 0; i < k; i++) {
            int term = y.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (x0 - x.get(j)) / (x.get(i) - x.get(j));
                }
            }
            secret += term;
        }
        return secret;
    }
}
