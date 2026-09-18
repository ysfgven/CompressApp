package exception;

public class CompressionException extends RuntimeException{


    public CompressionException(String message,Throwable cause){
        super(message,cause);
    }
}
