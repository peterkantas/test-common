package communication.http

import com.fasterxml.jackson.databind.JsonNode
import communication.common.CommonUtils
import communication.common.RequestType
import communication.http.HttpUtil.Companion.sendXMLRequest
import org.w3c.dom.Document
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HttpHelper {
    private val httpClient: HttpClient = makeSSLIgnoreHttpClient()

    private fun makeSSLIgnoreHttpClient(): HttpClient {
        return try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)
            }), SecureRandom())

            HttpClient.newBuilder().sslContext(sslContext).build()
        } catch (e: Exception) {
            throw RuntimeException("Error creating SSL-ignoring HTTP client", e)
        }
    }

    fun sendJsonRequestWithoutURLEncoding(url: String, requestString: String): String {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(HttpRequest.BodyPublishers.ofString(requestString))
            .build()

        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return httpResponse.body()
    }

    fun sendJSONRequest(
        apiUrl: String,
        commonRequest: String,
        headerName: String,
        headerValue: String,
        requestType: RequestType
    ): String {
        return try {
            println("Request --------------")
            if (requestType == RequestType.GET || requestType == RequestType.DELETE) {
                println("No request body, because of the request type.")
            } else {
                println(commonRequest)
            }
            println("Response --------------")

            val requestBuilder = HttpRequest.newBuilder().uri(URI.create(apiUrl))
                .header(headerName, headerValue)
                .header("Content-Type", "application/json")

            val request = when (requestType) {
                RequestType.POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(commonRequest)).build()
                RequestType.GET -> requestBuilder.GET().build()
                RequestType.DELETE -> requestBuilder.DELETE().build()
                RequestType.PUT, RequestType.PATCH -> throw UnsupportedOperationException("Method not implemented")
            }

            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body()
            println(response)
            response
        } catch (e: Exception) {
            throw RuntimeException("HTTP request failed", e)
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
            } catch (e: Exception) {
                throw RuntimeException("XML request failed", e)
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
            } catch (e: Exception) {
                throw RuntimeException("XML GET request failed", e)
            }
        }
    }
}
