package com.se.music.pastmusic;

import org.junit.Test;

import java.sql.SQLOutput;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testColor() {
        String s = Integer.toHexString(4334209);
        System.out.println(s);
        assertEquals(4, 2 + 2);
        String string = "111222333444555222";
        System.out.println("string.replace(\"222\",\"ddd\") = " + string.replace("222","ddd"));
        
    }
}