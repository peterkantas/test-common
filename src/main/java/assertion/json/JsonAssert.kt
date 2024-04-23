package assertion.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static reading.json.ReadJsonNode.readJsonResponseNode;


public class JsonAssert {
    public static void jsonAssertWithUrl(String url, String parent, String child, Integer childID, String keyValue, String expected) throws IOException {
        JsonNode jsonNode = readJsonResponseNode(url);
        if (expected == null) {
            expected = "null";
        }
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected);
    }

    public static void jsonAssertWithFile(JsonNode jsonNode, String parent, String child, Integer childID, String keyValue, String expected) {
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected);
    }

    public static void jsonAssertion(JsonNode jsonNode, String parent, String child, Integer childID, String keyValue, String expected) {
        String actual = null;
        try {
            if (!Objects.equals(parent, "") && Objects.equals(child, "") && Objects.equals(childID, null)
                    && Objects.equals(keyValue, "")) {
                actual = jsonNode.get(parent).asText();
                assertEquals(expected, actual);
            } else if (!Objects.equals(parent, "") && !Objects.equals(child, "")) {
                actual = jsonNode.get(parent).get(0).get(child).get(childID).get(keyValue).asText();
                assertEquals(expected, actual);
            } else if (parent.equals("") && !Objects.equals(child, "") && !Objects.equals(keyValue, "")) {
                actual = jsonNode.get(child).get(keyValue).asText();
                assertEquals(expected, actual);
            } else if (parent.equals("") && !Objects.equals(child, "") && keyValue.equals("")) {
                actual = jsonNode.get(child).asText().replace("\n", "").replace("\r", "");
                assertEquals(expected, actual);
            } else {
                System.out.println("Something is wrong.");
            }
        } catch (AssertionError aE) {
            if (keyValue.equals("")) {
                System.out.println("Failed to check the response.:" + child + "  Difference between expected and actual value Expected value: " + expected + " Actual value: " + actual);
                throw aE;
            } else {
                System.out.println("Failed to check the response.:" + keyValue + "   Difference between expected and actual value Expected value: " + expected + " Actual value: " + actual);
            }
        }
    }
}
