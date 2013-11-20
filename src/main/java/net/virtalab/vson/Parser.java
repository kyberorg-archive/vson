package net.virtalab.vson;

import java.lang.reflect.Type;

/**
 * Static class for using Vson with default methods
 */
public final class Parser {
    private static final Vson parser = new VsonBuilder().create();

    private Parser(){}

    public static <T> T fromJson(String json, Type typeOfT){
        return parser.fromJson(json, typeOfT);
    }
    public static String toJson(Object src, Class<?> typeOfSrc){
        return parser.toJson(src, typeOfSrc);
    }

}
