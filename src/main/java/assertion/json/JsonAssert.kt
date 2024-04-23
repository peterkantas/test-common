package assertion.json

import com.fasterxml.jackson.databind.JsonNode
import org.junit.jupiter.api.Assertions
import reading.json.ReadJsonNode
import java.io.IOException

object JsonAssert {
    @Throws(IOException::class)
    fun jsonAssertWithUrl(
        url: String,
        parent: String,
        child: String,
        childID: Int,
        keyValue: String,
        expected: String
    ) {
        val jsonNode = ReadJsonNode.readJsonResponseNode(url)
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected)
    }

    fun jsonAssertWithFile(
        jsonNode: JsonNode,
        parent: String,
        child: String,
        childID: Int,
        keyValue: String,
        expected: String
    ) {
        jsonAssertion(jsonNode, parent, child, childID, keyValue, expected)
    }

    fun jsonAssertion(
        jsonNode: JsonNode,
        parent: String,
        child: String,
        childID: Int,
        keyValue: String,
        expected: String
    ) {
        lateinit var actual: String
        try {
            if (parent != "" && child == "" && childID == null && keyValue == "") {
                actual = jsonNode[parent].asText()
                Assertions.assertEquals(expected, actual)
            } else if (parent != "" && child != "") {
                actual = jsonNode[parent][0][child][childID!!][keyValue].asText()
                Assertions.assertEquals(expected, actual)
            } else if (parent == "" && child != "" && keyValue != "") {
                actual = jsonNode[child][keyValue].asText()
                Assertions.assertEquals(expected, actual)
            } else if (parent == "" && child != "" && keyValue == "") {
                actual = jsonNode[child].asText().replace("\n", "").replace("\r", "")
                Assertions.assertEquals(expected, actual)
            } else {
                println("Something is wrong.")
            }
        } catch (aE: AssertionError) {
            if (keyValue == "") {
                println("Failed to check the response.:$child  Difference between expected and actual value Expected value: $expected Actual value: $actual")
                throw aE
            } else {
                println("Failed to check the response.:$keyValue   Difference between expected and actual value Expected value: $expected Actual value: $actual")
            }
        }
    }
}
