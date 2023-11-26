package communication.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.common.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpJsonUtil {

    public static StringBuilder response = new StringBuilder();
    public static URL setURL(String apiUrl) {
        URL url = null;
        try {
            url = new URL(apiUrl);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        return url;
    }

    public static HttpURLConnection setHttpConnection(URL url, RequestType requestType,String headerName,String headerValue) throws ProtocolException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        connection.setRequestMethod(String.valueOf(requestType));
        connection.setRequestProperty(headerName,headerValue);
        connection.setDoOutput(true);
        return connection;
    }

    public static void sendRequest(HttpURLConnection connection, String commonRequest) throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = commonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }

    public static String checkAndReturnResponse(HttpURLConnection connection) throws JsonProcessingException {
        try {
        int responseCode = connection.getResponseCode();
        System.out.println("Státuszkód: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("API válasz: " + response);
            }
        } else {
            System.out.println("Hiba az API hívás során. Válasz: " + readErrorResponse(connection));
        }

        connection.disconnect();

    } catch (Exception e) {
            System.out.println("Kivétel volt.: "+e);
    }
        return prettyPrintJson(response.toString());
    }

    private static String readErrorResponse(HttpURLConnection connection) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (Exception e) {
            System.out.println("Kivétel volt.: "+e);
            return "Nem sikerült az hiba választ beolvasni.";
        }
    }

    public static String prettyPrintJson(String uglyResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(uglyResponse, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }

    public static JsonNode castStringToJsonNode(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response);
    }
}
