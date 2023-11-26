package assertion.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static reading.json.ReadJsonNode.readJsonValaszNode;


public class JsonAssert {
    public static void jsonAssertWithUrl(String url, String parent, String child, int childID, String keyValue, String expected) throws IOException {
        JsonNode jsonNode = readJsonValaszNode(url);
        if (expected == null) {
            expected = "null";
        }
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected);
    }

    public static void jsonAssertWithFile(JsonNode jsonNode, String parent, String child, int childID, String keyValue, String expected) {
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected);
    }

    public static void jsonAssertion(JsonNode jsonNode, String parent, String child, int childID, String keyValue, String expected) {
        String actual = null;
        try {
            if (!Objects.equals(parent, "") && Objects.equals(child, "") && Objects.equals(String.valueOf(childID), "")
                    && Objects.equals(keyValue, "")) {
                actual = jsonNode.get(parent).asText();
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
                System.out.println("Itt valami nagyon nincs rendjén.");
            }
        } catch (AssertionError aE) {
            if (keyValue.equals("")) {
                System.out.println("Elbukott egy ellenőrzés a válasz vizsgálatában.:" + child + "  Különbség az elvárt és a tényleges érték között Elvárt érték: " + expected + " Tényleges érték: " + actual);
                throw aE;
            } else {
                System.out.println("Elbukott egy ellenőrzés a válasz vizsgálatában.:" + keyValue + "   Különbség az elvárt és a tényleges érték között Elvárt érték: " + expected + " Tényleges érték: " + actual);
            }
        }
    }
}
