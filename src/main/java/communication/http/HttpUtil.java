package communication.http;

import com.fasterxml.jackson.databind.JsonNode;
import communication.common.CommonUtils;
import communication.common.RequestType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static communication.common.CommonUtils.makeSSLIgnoreHttpClient;
import static communication.common.CommonUtils.returnDocumentTypeResponse;

public class HttpUtil {
    public static HttpRequest request;
    public static HttpClient httpClient = makeSSLIgnoreHttpClient();
    CommonUtils cu = new CommonUtils();

    public static Document sendXMLRequest(String requestBody, String requestURL, String[] headers, RequestType requestType) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        setRequestTypes(requestBody, requestURL, headers, requestType);
        return returnDocumentTypeResponse(httpClient);
    }

    private static void setRequestTypes(String requestBody, String requestURL, String[] headers, RequestType requestType) {
        switch (requestType) {
            //TODO: Ellenőrizni kell, hogyha null a header, akkor nem száll-e el NPE-re.
            case POST ->
                    request = HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(headers).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            case GET -> request = HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(headers).GET().build();
            case DELETE ->
                    request = HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(headers).DELETE().build();
        }

    }

    public JsonNode sendJsonRequestPOST(String apiUrl, String commonRequest, String headerName, String headerValue, RequestType requestType) throws IOException {
        return cu.returnJsonResponsePOST(apiUrl, commonRequest, headerName, headerValue, requestType);
    }

    public JsonNode sendJsonRequestGET(String url) throws IOException {
        return cu.returnJsonResponseGET(url);
    }

}
