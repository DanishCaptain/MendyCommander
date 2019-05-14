package org.mendybot.commander.android.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mendybot.commander.android.domain.Foo;

import static org.junit.Assert.*;

public class UrlUtilityTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void doIt() {
    }

    @Test
    public void exchangeJson() {
        String result = UrlUtility.exchangeJson("https://sspservices.pub.network/ssp-services/lookup/site?t=60aa4471-4219-483d-bc56-7b2ff581638b", "{}");
        assertNotNull(result);
        GsonBuilder b = new GsonBuilder();
        Gson g = b.create();
        Class<? extends Object> s = Foo.class;
        Foo f = (Foo) g.fromJson(result, s);
        assertNotNull(f);
        System.out.println(f);
    }
}