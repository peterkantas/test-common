package assertion.pipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PipeAssert {
    public static void pipeAssertion(String pipeResponse, Integer number, String assertingValue) {
        String[] splittedPipeResponse = pipeResponse.split("\\|");
        assertEquals(assertingValue, splittedPipeResponse[number]);
    }
}
