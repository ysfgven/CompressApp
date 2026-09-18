package exception;

public class DecompressionException extends RuntimeException{

    public DecompressionException(String message,Throwable cause){
        super(message,cause);
    }
    public DecompressionException(String message){
        super(message);
    }

}
