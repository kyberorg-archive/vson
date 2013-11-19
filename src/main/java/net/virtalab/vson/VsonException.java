package net.virtalab.vson;

/**
 * General application exception.
 */
public class VsonException extends RuntimeException {
    public VsonException(String message){
        super(message);
    }
    public VsonException(String message,Throwable cause){
        super(message, cause);
    }
    public VsonException(Throwable cause){
        super(cause);
    }
}
