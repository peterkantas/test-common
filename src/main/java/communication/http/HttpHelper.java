package communication.http;

import communication.common.CommonUtils;
import communication.common.RequestType;
import org.w3c.dom.Document;

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

  public static String sendPOSTJSONRequest(String apiUrl, String commonRequest, String headerName,String headerValue, RequestType requestType) {
        try {
            System.out.println("Kérdés --------------");
            System.out.println(commonRequest);
            System.out.println("Válasz --------------");
            var response = HttpUtil.sendJsonRequest(apiUrl,commonRequest,headerName,headerValue,requestType);
            System.out.println(response);
            return response;
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
