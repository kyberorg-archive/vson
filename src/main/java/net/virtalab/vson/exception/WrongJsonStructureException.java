package net.virtalab.vson.exception;

/**
 * Thrown when Json contains structure that doesn't corresponds with resulting class
 */
public class WrongJsonStructureException extends RuntimeException {
    public WrongJsonStructureException(String message){
        super(message);
    }
}
