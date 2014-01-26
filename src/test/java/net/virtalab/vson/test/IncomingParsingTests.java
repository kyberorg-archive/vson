package net.virtalab.vson.test;

import net.virtalab.vson.Vson;
import net.virtalab.vson.test.type.UptimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Parser Tests
 */
public class IncomingParsingTests extends Assert {
    private Vson vson;

    @Before
    public void prepare(){
        this.vson = new Vson();
    }

    public void customStringTest(){
        String str = "I am not a json at all";
    }

    public void emptyJsonTest(){
        String str = "{}";
    }
    public void badSyntaxJsonTest(){
        String str = "{'key\":1}";
    }

    public void wrongObjectImplementationTest(){
        String str = "{key:1, bad_field:fake}";
    }

    public void notAllFieldsPresentTest(){
        String str = "{key:1}";
    }

    public void fieldsWithNullTest(){
        String str = "{key:1, value: null }";
    }

    public void complexObjectTest(){

    }

    public void EcsapeCharactersTest(){

    }

    @Test
    public void wrongObjectPassedTest(){
        String str = "{\"test\":\"value\"}";
        UptimeFormat o = vson.fromJson(str, UptimeFormat.class);
        assertNotNull(o);
    }
}
