package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecompressionExceptionTest {
    @Test
    void constructor_withMessage_setsMessageCorrectly() {
        DecompressionException exception = new DecompressionException("Decompression failed");
        assertEquals("Decompression failed",  exception.getMessage());
    }

}