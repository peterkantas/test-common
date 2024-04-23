package reading.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.net.URL

object ReadJsonNode {
    @Throws(IOException::class)
    fun readJsonResponseNode(uri: String): JsonNode {
        val url = URL(uri)
        val objectMapper = ObjectMapper()
        return objectMapper.readTree(url)
    }
}
