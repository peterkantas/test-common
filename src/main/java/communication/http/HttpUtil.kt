package communication.http

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.GsonBuilder
import communication.common.CommonUtils
import communication.common.RequestType
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import javax.xml.parsers.ParserConfigurationException

class HttpUtil {
    var cu = CommonUtils()

    @kotlin.Throws(IOException::class)
    fun sendJsonRequestPOST(
        apiUrl: String,
        commonRequest: String,
        headerName: String,
        headerValue: String,
        requestType: RequestType
    ): JsonNode {
        return cu.returnJsonResponsePOST(apiUrl, commonRequest, headerName, headerValue, requestType)
    }

    @kotlin.Throws(IOException::class)
    fun sendJsonRequestGET(url: String): JsonNode {
        return cu.returnJsonResponseGET(url)
    }

    @kotlin.Throws(IOException::class)
    fun sendJsonRequestDELETE(url: String): JsonNode {
        return cu.returnJsonResponseDELETE(url)
    }

    companion object {
        @kotlin.jvm.JvmField
        var request: HttpRequest? = null
        var httpClient = CommonUtils.makeSSLIgnoreHttpClient()

        @kotlin.jvm.JvmStatic
        @kotlin.Throws(
            IOException::class,
            InterruptedException::class,
            SAXException::class,
            ParserConfigurationException::class
        )
        fun sendXMLRequest(
            requestBody: String,
            requestURL: String,
            headers: Array<String>,
            requestType: RequestType
        ): Document {
            setRequestTypes(requestBody, requestURL, headers, requestType)
            return CommonUtils.returnDocumentTypeResponse(httpClient)
        }

        private fun setRequestTypes(
            requestBody: String,
            requestURL: String,
            headers: Array<String>,
            requestType: RequestType
        ) {
            request = when (requestType) {
                RequestType.POST -> HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(*headers).POST(
                    HttpRequest.BodyPublishers.ofString(requestBody)
                ).build()

                RequestType.GET -> HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(*headers).GET().build()

                RequestType.DELETE -> HttpRequest.newBuilder().uri(URI.create(requestURL)).headers(*headers).DELETE()
                    .build()

                RequestType.PUT -> TODO()
                RequestType.PATCH -> TODO()
            }
        }
    }
}
