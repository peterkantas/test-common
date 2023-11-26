package communication.http;

import com.fasterxml.jackson.databind.JsonNode;
import communication.common.CommonUtils;
import communication.common.RequestType;
import org.w3c.dom.Document;

import static communication.http.HttpJsonUtil.castStringToJsonNode;

public class HttpHelper {

    public static Document sendPOSTXMLRequest(String requestURL, String[] headers, String requestBody) {
        try {
            System.out.println("Kérdés --------------");
            System.out.println(requestBody);
            System.out.println("Válasz --------------");
            var response = HttpUtil.sendXMLRequest(requestBody, requestURL, headers, RequestType.POST);
            CommonUtils.printDocument(response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

  public static JsonNode sendPOSTJSONRequest(String apiUrl, String commonRequest, String headerName, String headerValue, RequestType requestType) {
        try {
            System.out.println("Kérdés --------------");
            System.out.println(commonRequest);
            System.out.println("Válasz --------------");
            var response = HttpUtil.sendJsonRequest(apiUrl,commonRequest,headerName,headerValue,requestType);
            System.out.println(response);
            return castStringToJsonNode(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document sendGETRequest(String requestURL, String[] headers, String requestBody) {
        try {

            System.out.println("Kérdés --------------");
            System.out.println(requestBody);
            System.out.println("Válasz --------------");
            var response = HttpUtil.sendXMLRequest(requestBody, requestURL, headers, RequestType.GET);
            CommonUtils.printDocument(response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
