package communication.common;

import com.fasterxml.jackson.databind.JsonNode;
import communication.http.HttpJsonUtil;
import communication.http.HttpUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static communication.http.HttpJsonUtil.*;

public class CommonUtils {
    private static HttpClient httpClient;
    HttpJsonUtil hju = new HttpJsonUtil();

    public static HttpClient makeSSLIgnoreHttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            httpClient = HttpClient.newBuilder().sslContext(sslContext).build();
        } catch (Exception e) {
            System.out.println("Exception.: " + e);
        }
        return httpClient;
    }

    public static void printDocument(Document xmlDocument) {
        Transformer tform = null;
        try {
            tform = TransformerFactory.newInstance().newTransformer();
            tform.setOutputProperty(OutputKeys.INDENT, "yes");
            tform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            tform.transform(new DOMSource(xmlDocument), new StreamResult(System.out));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document returnDocumentTypeResponse(HttpClient ignoreSSLHttpClient) throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        HttpResponse<String> response = ignoreSSLHttpClient.send(HttpUtil.request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(response.body())));
    }

    public JsonNode returnJsonResponseGET(String apiUrl) throws IOException {
        return hju.sendJsonRequestGET(apiUrl);
    }

    public JsonNode returnJsonResponseDELETE(String apiUrl) throws IOException {
        return hju.sendJsonRequestDELETE(apiUrl);
    }

    public JsonNode returnJsonResponsePOST(String apiUrl, String commonRequest, String headerName, String headerValue, RequestType requestType) throws IOException {
        URL url = setURL(apiUrl);
        HttpURLConnection connection = setHttpConnection(url, requestType, headerName, headerValue);
        sendJsonRequestPOST(connection, commonRequest);
        return hju.castStringToJsonNode(hju.checkAndReturnResponse(connection));
    }
}
