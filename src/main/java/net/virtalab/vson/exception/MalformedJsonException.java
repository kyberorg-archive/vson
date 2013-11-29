package net.virtalab.vson.exception;

/**
 * Thrown when Strict parsing failed
 */
public class MalformedJsonException extends VsonException {
    /**
     * Constructs exception from provided message
     * @param message message indicates reason
     */
      public MalformedJsonException(String message){
          super("Malformed JSON: "+message);
      }
}
