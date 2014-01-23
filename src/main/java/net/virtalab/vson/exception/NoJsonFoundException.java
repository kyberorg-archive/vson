package net.virtalab.vson.exception;

/**
 * Thrown when Strict parsing failed
 */
public class NoJsonFoundException extends VsonException {
    /**
     * Constructs exception from provided message
     * @param message message indicates reason
     */
      public NoJsonFoundException(){
          super("Provided String is not valid JSON");
      }
}
