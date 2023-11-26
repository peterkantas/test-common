package communication.http;

import communication.common.RequestType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static communication.common.CommonUtils.*;
import static communication.http.HttpJsonUtil.setHttpConnection;
import static communication.http.HttpJsonUtil.setURL;

public class HttpUtil {

    public static HttpRequest request;
    public static HttpClient httpClient = makeSSLIgnoreHttpClient();

    public static Document sendXMLRequest(String requestBody, String requestURL, String[] headers, RequestType requestType) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        setRequestTypes(requestBody, requestURL, headers, requestType);
        return returnDocumentTypeResponse(httpClient);
    }

    public static String sendJsonRequest(String apiUrl, String commonRequest, String headerName,String headerValue, RequestType requestType) throws IOException {
        return returnJsonResponse(apiUrl,commonRequest,headerName,headerValue,requestType);
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

}
