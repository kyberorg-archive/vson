package net.virtalab.vson.exception;

/**
 * General application exception
 */
public class VsonException extends RuntimeException {
    /**
     * Constructs exception from given message
     * @param message message indicating fail reason
     */
    public VsonException(String message){
        super(message);
    }

    /**
     * Construct exception from given message and exception
     * @param message message indicating fail reason
     * @param cause chained exception that caused this exception
     */
    public VsonException(String message,Throwable cause){
        super(message, cause);
    }

    /**
     * Construct exception from chained exception
     * @param cause chained exception that caused this exception
     */
    public VsonException(Throwable cause){
        super(cause);
    }
}
