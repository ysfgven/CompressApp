package exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidFormatExceptionTest {
    @Test
    void constructor_setsReasonAndMessageCorrectly() {
        InvalidFormatException exception = new InvalidFormatException(InvalidFormatException.Reason.MAGIC_NUMBER, "Invalid magic number in header");

        assertEquals(InvalidFormatException.Reason.MAGIC_NUMBER,exception.getReason());
        assertEquals("Invalid magic number in header", exception.getMessage());
    }

    @Test
    void reasonEnum_containsAllExpectedValues() {
        InvalidFormatException.Reason[] values = InvalidFormatException.Reason.values();
        assertEquals(3, values.length);
        assertArrayEquals(new InvalidFormatException.Reason[]{InvalidFormatException.Reason.MAGIC_NUMBER, InvalidFormatException.Reason.UNSUPPORTED_VERSION, InvalidFormatException.Reason.CORRUPTED_HEADER},values);
    }

}