package assertion.pipe

import org.junit.jupiter.api.Assertions

object PipeAssert {
    fun pipeAssertion(pipeResponse: String, number: Int, assertingValue: String) {
        val splittedPipeResponse = pipeResponse.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Assertions.assertEquals(assertingValue, splittedPipeResponse[number])
    }
}
