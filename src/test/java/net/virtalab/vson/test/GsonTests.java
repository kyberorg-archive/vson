package net.virtalab.vson.test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.virtalab.vson.test.type.MyJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Edit this.
 */
public class GsonTests extends Assert {
    private Gson gson;

    @Before
    public void prepare(){
        this.gson = new Gson();
    }

    @Test(expected = Exception.class)
    public void customStringTest(){
        //JSE <- IllegalStateE
        String str = "{} I am not JSON";
        MyJson o = gson.fromJson(str, MyJson.class);
        assertTrue(true);
    }

    @Test(expected = JsonSyntaxException.class)
    public void badSyntaxJsonTest(){
        //JSE <- MalformedJsonE
        String str = "{\"key\":1,}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertTrue(true);
    }

    @Test(expected = JsonSyntaxException.class)
    public void malformedJsonTest(){
        //JSE <- MalformedJsonE
        String str = "{'key\":1}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertTrue(o.getKey()==1);
    }

    @Test
    public void emptyJsonTest(){
        //passed
        //key - null, value - null
        String str = "{}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertTrue(o!=null);
    }
    @Test
    public void badObjectImplTest(){
        //key - 1, value = null
        String str = "{key:1, bad_field:fake}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertTrue(o.getKey() != 0);
    }

    @Test
    public void badTypeAtObjectImplTest(){
        //JSE <- ISE (norm explain)
        String str = "{key:ss, value:aaa}";
        try{
            MyJson o = gson.fromJson(str,MyJson.class);
        }catch (JsonSyntaxException jse){
            String message = jse.getCause().getMessage();

            throw new RuntimeException(message);
        }
    }

    @Test
    public void nullObjectImplTest(){
        //key - 1, value = null
        String str = "{key:1, value:null}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertTrue(o.getValue() == null);
    }

    @Test
    public void wrongObjectPassedTest(){
        String str = "{\"test\":\"value\"}";
        MyJson o = gson.fromJson(str,MyJson.class);
        assertNotNull(o);
    }

}
