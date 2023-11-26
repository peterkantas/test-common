package communication.http;

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

    public static Document sendRequest(String requestBody, String requestURL, String[] headers, RequestType requestType) throws IOException, InterruptedException, SAXException, ParserConfigurationException {

        switch (requestType) {
            //TODO: Ellenőrizni kell, hogyha null a header, akkor nem száll-e el NPE-re.
            case POST ->
                    request = HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(headers).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            case GET -> request = HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(headers).GET().build();
        }
        HttpClient httpClient = makeSSLIgnoreHttpClient();
        return returnDocumentTypeResponse(httpClient);
    }

}
