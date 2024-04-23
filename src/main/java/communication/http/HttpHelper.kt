package communication.http;

import com.fasterxml.jackson.databind.JsonNode;
import communication.common.CommonUtils;
import communication.common.RequestType;
import org.w3c.dom.Document;

public class HttpHelper {
    HttpUtil httpUtil = new HttpUtil();

    public static Document sendPOSTXMLRequest(String requestURL, String[] headers, String requestBody) {
        try {
            System.out.println("Request --------------");
            System.out.println(requestBody);
            System.out.println("Response --------------");
            var response = HttpUtil.sendXMLRequest(requestBody, requestURL, headers, RequestType.POST);
            CommonUtils.printDocument(response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document sendXMLRequestGET(String requestURL, String[] headers, String requestBody) {
        try {

            System.out.println("Request --------------");
            System.out.println(requestBody);
            System.out.println("Response --------------");
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
            System.out.println("Request --------------");
            if (requestType.equals(RequestType.GET) ||requestType.equals(RequestType.DELETE)) {
                System.out.println("No request body, because of the request type.");    
            } else {
                System.out.println(commonRequest);
            }
            System.out.println("Response --------------");
            switch (requestType) {
                case POST ->
                        response = httpUtil.sendJsonRequestPOST(apiUrl, commonRequest, headerName, headerValue, requestType);
                case GET -> response = httpUtil.sendJsonRequestGET(apiUrl);
                case DELETE -> response = httpUtil.sendJsonRequestDELETE(apiUrl);
            }
            System.out.println(response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
