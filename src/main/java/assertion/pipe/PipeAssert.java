package assertion.pipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PipeAssert {
    public static void pipeAssertion(String pipeResponse, Integer sorSzam, String vizsgalandoErtek) {
        String[] splittedPipeResponse = pipeResponse.split("\\|");
        assertEquals(vizsgalandoErtek, splittedPipeResponse[sorSzam]);
    }
}
