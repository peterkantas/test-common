package communication.http

import com.fasterxml.jackson.databind.JsonNode
import communication.common.CommonUtils
import communication.common.RequestType
import communication.http.HttpUtil.Companion.sendXMLRequest
import org.w3c.dom.Document

class HttpHelper {
    var httpUtil = HttpUtil()
    fun sendJSONRequest(
        apiUrl: String,
        commonRequest: String,
        headerName: String,
        headerValue: String,
        requestType: RequestType
    ): JsonNode {
        val response: JsonNode
        return try {
            println("Request --------------")
            if (requestType == RequestType.GET || requestType == RequestType.DELETE) {
                println("No request body, because of the request type.")
            } else {
                println(commonRequest)
            }
            println("Response --------------")
            response = when (requestType) {
                RequestType.POST -> httpUtil.sendJsonRequestPOST(
                    apiUrl,
                    commonRequest,
                    headerName,
                    headerValue,
                    requestType
                )

                RequestType.GET -> httpUtil.sendJsonRequestGET(apiUrl)
                RequestType.DELETE -> httpUtil.sendJsonRequestDELETE(apiUrl)
                RequestType.PUT -> TODO()
                RequestType.PATCH -> TODO()
            }
            println(response)
            response
        } catch (e: java.lang.Exception) {
            throw java.lang.RuntimeException(e)
        }
    }

    companion object {
        fun sendPOSTXMLRequest(requestURL: String, headers: Array<String>, requestBody: String): Document {
            return try {
                println("Request --------------")
                println(requestBody)
                println("Response --------------")
                val response = sendXMLRequest(requestBody, requestURL, headers, RequestType.POST)
                CommonUtils.printDocument(response)
                response
            } catch (e: java.lang.Exception) {
                throw java.lang.RuntimeException(e)
            }
        }

        fun sendXMLRequestGET(requestURL: String, headers: Array<String>, requestBody: String): Document {
            return try {
                println("Request --------------")
                println(requestBody)
                println("Response --------------")
                val response = sendXMLRequest(requestBody, requestURL, headers, RequestType.GET)
                CommonUtils.printDocument(response)
                response
            } catch (e: java.lang.Exception) {
                throw java.lang.RuntimeException(e)
            }
        }
    }
}
