package net.virtalab.vson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.virtalab.vson.annotation.EmptyAllowed;
import net.virtalab.vson.annotation.Optional;

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
    private Gson myGson;

    boolean stopOnFirstError = true;

    public Vson(){
        this.myGson = new GsonBuilder().create();
    }

    public <T> T fromJson(String json, Type typeOfT) throws VsonException {
        T jsonedObject;
        try{
           jsonedObject = myGson.fromJson(json,typeOfT);
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
                    if(f.getAnnotation(EmptyAllowed.class) ==null){
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

    public String toJson(Object src, Class<?> typeOfSrc){
        return myGson.toJson(src, typeOfSrc);
    }

    private enum ErrorType{
        FIELD_NULL,
        FIELD_EMPTY;
    }

    private enum FieldType{
        STRING,
        ARRAY,
        COLLECTION;
    }

    private class ErrorStruct{
        String fieldName;
        ErrorType errorType;
        FieldType fieldType;

        ErrorStruct(String fieldName){
            this.fieldName = fieldName;
            this.errorType = ErrorType.FIELD_NULL;
        }
        ErrorStruct(String fieldName, FieldType fieldType){
            this.fieldName = fieldName;
            this.errorType = ErrorType.FIELD_EMPTY;
            this.fieldType = fieldType;
        }
    }

}
