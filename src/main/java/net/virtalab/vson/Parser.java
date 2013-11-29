package net.virtalab.vson;

import net.virtalab.vson.exception.VsonException;

import java.lang.reflect.Type;

/**
 * Static class for using Vson with default settings
 */
public final class Parser {
    /**
     * VSON Parser object
     */
    private static final Vson parser = new VsonBuilder().create();

    /**
     * No need to init this class
     */
    private Parser(){}

    /**
     * Creates object of given Type from JSON (its String representation)
     *
     * @param json JSON as String
     * @param typeOfT Class of resulting object
     * @param <T> Generic for resulting type
     * @return Resulting object of Type which defined in typeOfT
     * @throws VsonException when parsing fails
     */
    public static <T> T fromJson(String json, Type typeOfT) throws VsonException{
        return parser.fromJson(json, typeOfT);
    }

    /**
     * Creates String with JSON from provided Object
     *
     * @param src Object from which JSON should be created
     * @param typeOfSrc Class of object given at src param
     * @return string that represent Object as JSON
     */
    public static String toJson(Object src, Class<?> typeOfSrc){
        return parser.toJson(src, typeOfSrc);
    }

}
