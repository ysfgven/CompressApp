package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompressionExceptionTest {
    @Test
    void constructor_withMessage_setsMessageCorrectly() {
        CompressionException exception = new CompressionException("Error");
        assertEquals("Error", exception.getMessage());
    }

    @Test
    void constructor_withMessageAndCause_setsBothCorrectly() {
        Throwable cause = new RuntimeException("Root cause");
        CompressionException exception = new CompressionException("Error", cause);

        assertEquals("Error",exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}