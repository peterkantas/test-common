package reading.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class ReadJsonNode {

    public static JsonNode readJsonValaszNode(String uri) throws IOException {
        URL url = new URL(uri);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(url);
    }
}
