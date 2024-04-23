package communication.http

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import communication.common.RequestType
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.nio.charset.StandardCharsets

class HttpJsonUtil {
    var response = StringBuilder()
    @Throws(JsonProcessingException::class)
    fun sendJsonRequestGET(url: String): JsonNode {
        lateinit var responseBody: String
        try {
            val httpClient: HttpClient = HttpClients.createDefault()
            val httpGet = HttpGet(url)
            val response = httpClient.execute(httpGet)
            if (response.statusLine.statusCode == 200 ||
                response.statusLine.statusCode == 404
            ) {
                responseBody = EntityUtils.toString(response.entity)
                println("API response: $responseBody")
            } else {
                println("HTTP Request failed with status code: " + response.statusLine.statusCode)
            }
        } catch (e: Exception) {
            println("Exception.: $e")
        }
        return castStringToJsonNode(responseBody)
    }

    @Throws(JsonProcessingException::class)
    fun sendJsonRequestDELETE(url: String): JsonNode {
        lateinit var responseBody: String
        try {
            val httpClient: HttpClient = HttpClients.createDefault()
            val httpDel = HttpDelete(url)
            val response = httpClient.execute(httpDel)
            if (response.statusLine.statusCode == 200) {
                responseBody = EntityUtils.toString(response.entity)
                println("API response: $responseBody")
            } else {
                println("HTTP Request failed with status code: " + response.statusLine.statusCode)
            }
        } catch (e: Exception) {
            println("Exception.: $e")
        }
        return castStringToJsonNode(responseBody)
    }

    @Throws(JsonProcessingException::class)
    fun checkAndReturnResponse(connection: HttpURLConnection): String {
        try {
            val responseCode = connection.getResponseCode()
            println("Status Code: $responseCode")
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(
                    InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)
                ).use { br ->
                    var responseLine: String
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine.trim { it <= ' ' })
                    }
                    println("API response: $response")
                }
            } else {
                println("HTTP Request failed with status code. Response.: " + readErrorResponse(connection))
            }
            connection.disconnect()
        } catch (e: Exception) {
            println("Exception.: $e")
        }
        return prettyPrintJson(response.toString())
    }

    @Throws(JsonProcessingException::class)
    fun castStringToJsonNode(response: String): JsonNode {
        val mapper = ObjectMapper()
        return mapper.readTree(response)
    }

    companion object {
        @JvmStatic
        fun setURL(apiUrl: String): URL {
            val url: URL = try {
                URL(apiUrl)
            } catch (ex: MalformedURLException) {
                throw RuntimeException(ex)
            }
            return url
        }

        @JvmStatic
        @Throws(ProtocolException::class)
        fun setHttpConnection(
            url: URL,
            requestType: RequestType,
            headerName: String,
            headerValue: String
        ): HttpURLConnection {
            val connection: HttpURLConnection = try {
                url.openConnection() as HttpURLConnection
            } catch (ex: IOException) {
                throw RuntimeException(ex)
            }
            connection.setRequestMethod(requestType.toString())
            connection.setRequestProperty(headerName, headerValue)
            connection.setDoOutput(true)
            return connection
        }

        @JvmStatic
        @Throws(IOException::class)
        fun sendJsonRequestPOST(connection: HttpURLConnection, commonRequest: String) {
            connection.outputStream.use { os ->
                val input = commonRequest.toByteArray(StandardCharsets.UTF_8)
                os.write(input, 0, input.size)
            }
        }

        private fun readErrorResponse(connection: HttpURLConnection): String {
            try {
                BufferedReader(
                    InputStreamReader(connection.errorStream, StandardCharsets.UTF_8)
                ).use { br ->
                    val response = StringBuilder()
                    var responseLine: String
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine.trim { it <= ' ' })
                    }
                    return response.toString()
                }
            } catch (e: Exception) {
                println("Exception.: $e")
                return "It was not possible to read the error response."
            }
        }

        @Throws(JsonProcessingException::class)
        fun prettyPrintJson(uglyResponse: String): String {
            val objectMapper = ObjectMapper()
            val jsonObject = objectMapper.readValue(uglyResponse, Any::class.java)
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject)
        }
    }
}
