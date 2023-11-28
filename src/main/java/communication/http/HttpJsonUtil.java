package communication.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.common.RequestType;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

    public StringBuilder response = new StringBuilder();

    public static URL setURL(String apiUrl) {
        URL url = null;
        try {
            url = new URL(apiUrl);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        return url;
    }

    public static HttpURLConnection setHttpConnection(URL url, RequestType requestType, String headerName, String headerValue) throws ProtocolException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        connection.setRequestMethod(String.valueOf(requestType));
        connection.setRequestProperty(headerName, headerValue);
        connection.setDoOutput(true);
        return connection;
    }

    public static void sendJsonRequestPOST(HttpURLConnection connection, String commonRequest) throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = commonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
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
            System.out.println("Exception.: " + e);
            return "It was not possible to read the error response.";
        }
    }

    public static String prettyPrintJson(String uglyResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(uglyResponse, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }

    public JsonNode sendJsonRequestGET(RequestType requestType,String url) throws JsonProcessingException {
        String responseBody = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200 ||
                response.getStatusLine().getStatusCode() == 404) {
                responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("API response: " + responseBody);
            } else {
                System.out.println("HTTP Request failed with status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Exception.: " + e);
        }
        return castStringToJsonNode(responseBody);
    }

    public JsonNode sendJsonRequestDELETE(String url) throws JsonProcessingException {
        String responseBody = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDel = new HttpDelete(url);
            HttpResponse response = httpClient.execute(httpDel);
            if (response.getStatusLine().getStatusCode() == 200) {
                responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("API response: " + responseBody);
            } else {
                System.out.println("HTTP Request failed with status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Exception.: " + e);
        }
        return castStringToJsonNode(responseBody);
    }

    public String checkAndReturnResponse(HttpURLConnection connection) throws JsonProcessingException {
        try {
            int responseCode = connection.getResponseCode();
            System.out.println("Status Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("API response: " + response);
                }
            } else {
                System.out.println("HTTP Request failed with status code. Response.: " + readErrorResponse(connection));
            }

            connection.disconnect();

        } catch (Exception e) {
            System.out.println("Exception.: " + e);
        }
        return prettyPrintJson(response.toString());
    }

    public JsonNode castStringToJsonNode(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response);
    }
}
