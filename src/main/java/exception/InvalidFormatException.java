package exception;

public class InvalidFormatException extends RuntimeException {

    private final Reason reason;

    public enum Reason {
        MAGIC_NUMBER,
        UNSUPPORTED_VERSION,
        CORRUPTED_HEADER
    }
    public InvalidFormatException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}


