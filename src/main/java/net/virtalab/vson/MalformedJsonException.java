package net.virtalab.vson;

/**
 * Class description.
 */
public class MalformedJsonException extends VsonException {
      public MalformedJsonException(String message){
          super("Malformed JSON: "+message);
      }
}
