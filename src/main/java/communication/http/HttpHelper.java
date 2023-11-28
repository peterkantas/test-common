package communication.http;

import com.fasterxml.jackson.databind.JsonNode;
import communication.common.CommonUtils;
import communication.common.RequestType;
import org.w3c.dom.Document;

public class HttpHelper {
    HttpUtil httpUtil = new HttpUtil();

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

    public static Document sendXMLRequestGET(String requestURL, String[] headers, String requestBody) {
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

    public JsonNode sendJSONRequest(String apiUrl, String commonRequest, String headerName, String headerValue, RequestType requestType) {
        JsonNode response = null;
        try {
            System.out.println("Kérdés --------------");
            System.out.println(commonRequest);
            System.out.println("Válasz --------------");
            switch (requestType) {
                case POST ->
                        response = httpUtil.sendJsonRequestPOST(apiUrl, commonRequest, headerName, headerValue, requestType);
                case GET -> response = httpUtil.sendJsonRequestGET(apiUrl);
            }
            System.out.println(response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
