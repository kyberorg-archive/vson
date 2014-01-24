package net.virtalab.vson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.virtalab.vson.annotation.EmptyValueAllowed;
import net.virtalab.vson.annotation.Optional;
import net.virtalab.vson.exception.MalformedJsonException;
import net.virtalab.vson.exception.NoJsonFoundException;
import net.virtalab.vson.exception.VsonException;
import net.virtalab.vson.exception.WrongJsonStructureException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Gson wrapper with dematerialized object validation
 */
public class Vson {
    /**
     * Google GSON instance
     */
    private Gson myGson;
    /**
     * Property that indicates that parser must stop just after first fail and do not collect all fails with given JSON
     */
    boolean stopOnFirstError = true;

    /**
     * Constructs parser instance and creates GSON with default options
     */
    //TODO Property for disableHtmlEscaping
    public Vson(){
        this.myGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * Creates object of given Type from JSON (its String representation)
     *
     * @param json JSON as String
     * @param typeOfT Class of resulting object
     * @param <T> Generic for resulting type
     * @return Resulting object of Type which defined in typeOfT
     * @throws VsonException when parsing fails
     * @throws com.google.gson.JsonSyntaxException when passed String contains JSON with broken syntax
     */
    public <T> T fromJson(String json, Type typeOfT) throws VsonException {
        T jsonedObject;
        try{
           jsonedObject = myGson.fromJson(json,typeOfT);
        }catch (JsonSyntaxException jse){
            if(jse.getCause() instanceof IllegalStateException){
                //when Json is malformed (or not Json at all) - explanation is Okay
                String message = jse.getCause().getMessage();
                if(message.contains("BEGIN_OBJECT")){
                    //Not a JSON
                    throw new NoJsonFoundException();
                } else {
                    //Param error
                    throw new WrongJsonStructureException();//update it
                }
            } else if(jse.getCause() instanceof MalformedJsonException){
                //when syntax is incorrect - explanation is Okay
                throw new JsonSyntaxException(jse.getCause().getMessage());
            } else {
                jsonedObject=null;
            }
        }catch (JsonParseException jpe){
            throw new VsonException(jpe);
        }


        if(jsonedObject==null){ throw new VsonException("Cannot operate on empty object"); }

        List<ErrorStruct> errors = new ArrayList<ErrorStruct>();

        Field[] fields = jsonedObject.getClass().getDeclaredFields();

        for(Field f: fields){
            f.setAccessible(true);

            Object fValue = null;
            try{
               fValue = f.get(jsonedObject);
            }catch (IllegalAccessException iae){
                //we are here when unable to get field value because Java Language Policies
                throw new VsonException(iae);
            }

            //check on null field
            if(fValue ==null){
                if(f.getAnnotation(Optional.class) ==null){
                    if(stopOnFirstError){
                        throw new MalformedJsonException("Field "+f.getName().toUpperCase()+" is not present or null.");
                    } else {
                        errors.add(new ErrorStruct(f.getName()));
                    }
                }
            }

            //check on empty String
            if(fValue  instanceof String){
                String s = (String) fValue;
                if(s.isEmpty()){
                    if(f.getAnnotation(EmptyValueAllowed.class) ==null){
                        if(stopOnFirstError){
                            throw new MalformedJsonException("Value of "+f.getName().toUpperCase()+ " cannot be empty.");
                        } else {
                            errors.add(new ErrorStruct(f.getName(),FieldType.STRING));
                        }
                    }
                }
            }

             //check on empty array
             if(f.getType().isArray()){
                int arrLenght = Array.getLength(fValue);
                if(arrLenght == 0){
                    if(stopOnFirstError){
                        throw new MalformedJsonException("Value of "+f.getName().toUpperCase()+ " must have at least one element.");
                    } else {
                        errors.add(new ErrorStruct(f.getName(),FieldType.ARRAY));
                    }
                }
             }

             //check on empty collection
             if(fValue instanceof Collection){
                Collection c = (Collection) fValue;
                if(c.isEmpty()){
                    if(stopOnFirstError){
                            throw new MalformedJsonException("Value of "+f.getName().toUpperCase()+ " must have at least one element.");
                        } else {
                            errors.add(new ErrorStruct(f.getName(),FieldType.COLLECTION));
                        }
                    }
                }
        }

        //error reporting
        StringBuilder sb = new StringBuilder();
        for(ErrorStruct error: errors){
            if(error.errorType==ErrorType.FIELD_NULL){
                sb.append("Field ").append(error.fieldName).append(" is not present or null.");
                sb.append(" ");
            }
            if(error.errorType==ErrorType.FIELD_EMPTY){
                switch (error.fieldType){
                    case STRING:
                        sb.append("Value of ").append(error.fieldName).append(" cannot be empty.");
                        sb.append(" ");
                        break;
                    case ARRAY:
                    case COLLECTION:
                        sb.append("Value of ").append(error.fieldName).append(" must have at least one element.");
                        sb.append(" ");
                        break;
                }
            }
        }

        return jsonedObject;
    }

    /**
     * Creates String with JSON from provided Object
     *
     * @param src Object from which JSON should be created
     * @param typeOfSrc Class of object given at src param
     * @return string that represent Object as JSON
     */
    public String toJson(Object src, Class<?> typeOfSrc){
        return myGson.toJson(src, typeOfSrc);
    }

    /**
     * Enum with error reason
     */
    private enum ErrorType{
        /**
         * Field not present or null
         */
        FIELD_NULL,
        /**
         * Field filed with empty values (for String: 0-char String, for Arrays and Collections: when it has no values)
         */
        FIELD_EMPTY
    }

    /**
     * Enum indicates nature of field
     */
    private enum FieldType{
        /**
         * line with chars (java.lang.String)
         */
        STRING,
        /**
         * Simple Java Array
         */
        ARRAY,
        /**
         * Structure that implements Collection (for example Map,Set,List)
         */
        COLLECTION
    }

    /**
     * Describes Error Structure
     */
    private class ErrorStruct{
        /**
         * name of field
         */
        String fieldName;
        /**
         * Reason of error (@see #ErrorType)
         */
        ErrorType errorType;
        /**
         * Nature of field type (@see #FieldType)
         */
        FieldType fieldType;

        /**
         * Constructs struct from only field name.
         * Used when we have NULL field (@see #ErrorType.FIELD_NULL)
         *
         * @param fieldName name of field for error reporting
         */
        ErrorStruct(String fieldName){
            this.fieldName = fieldName;
            this.errorType = ErrorType.FIELD_NULL;
        }

        /**
         * Used to construct object when field is empty (@see #ErrorType.FIELD_EMPTY for emptiness criteria)
         *
         * @param fieldName name of field for error reporting
         * @param fieldType nature of field's type (@see #FieldType)
         */
        ErrorStruct(String fieldName, FieldType fieldType){
            this.fieldName = fieldName;
            this.errorType = ErrorType.FIELD_EMPTY;
            this.fieldType = fieldType;
        }
    }

}
