package communication.common

import com.fasterxml.jackson.databind.JsonNode
import communication.http.HttpJsonUtil
import communication.http.HttpJsonUtil.Companion.sendJsonRequestPOST
import communication.http.HttpJsonUtil.Companion.setHttpConnection
import communication.http.HttpJsonUtil.Companion.setURL
import communication.http.HttpUtil
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class CommonUtils {
    var hju = HttpJsonUtil()
    @Throws(IOException::class)
    fun returnJsonResponseGET(apiUrl: String): JsonNode {
        return hju.sendJsonRequestGET(apiUrl)
    }

    @Throws(IOException::class)
    fun returnJsonResponseDELETE(apiUrl: String): JsonNode {
        return hju.sendJsonRequestDELETE(apiUrl)
    }

    @Throws(IOException::class)
    fun returnJsonResponsePOST(
        apiUrl: String,
        commonRequest: String,
        headerName: String,
        headerValue: String,
        requestType: RequestType
    ): JsonNode {
        val url = setURL(apiUrl)
        val connection = setHttpConnection(url, requestType, headerName, headerValue)
        sendJsonRequestPOST(connection, commonRequest)
        return hju.castStringToJsonNode(hju.checkAndReturnResponse(connection))
    }

    companion object {
        private lateinit var httpClient: HttpClient
        fun makeSSLIgnoreHttpClient(): HttpClient {
            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }
                }), SecureRandom())
                httpClient = HttpClient.newBuilder().sslContext(sslContext).build()
            } catch (e: Exception) {
                println("Exception.: $e")
            }
            return httpClient
        }

        fun printDocument(xmlDocument: Document?) {
            val tform: Transformer
            try {
                tform = TransformerFactory.newInstance().newTransformer()
                tform.setOutputProperty(OutputKeys.INDENT, "yes")
                tform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
                tform.transform(DOMSource(xmlDocument), StreamResult(System.out))
            } catch (e: TransformerException) {
                throw RuntimeException(e)
            }
        }

        @Throws(
            IOException::class,
            InterruptedException::class,
            ParserConfigurationException::class,
            SAXException::class
        )
        fun returnDocumentTypeResponse(ignoreSSLHttpClient: HttpClient): Document {
            val response = ignoreSSLHttpClient.send(
                HttpUtil.request, HttpResponse.BodyHandlers.ofString(
                    StandardCharsets.UTF_8
                )
            )
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            return builder.parse(InputSource(StringReader(response.body())))
        }
    }
}
