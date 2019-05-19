package org.mendybot.commander.android.tools;

import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mendybot.commander.android.domain.AudioFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.TvShow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

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
    public void exchangeMovieJson() {
//        String json = "[{\"title\": \"Harry Potter and the Deathly Hallows\", \"fileName\": \"hp/hp7a.mp3\"}]";
//        String result = UrlUtility.exchangeJson("https://sspservices.pub.network/ssp-services/lookup/site?t=60aa4471-4219-483d-bc56-7b2ff581638b", "{}");
//        String result = UrlUtility.exchangeJson("http://192.168.100.50:21121/audio", json);
        String result = UrlUtility.grabJson("http://192.168.100.50:21123/movies");
//        String result = UrlUtility.grabJson("http://192.168.100.50:21124/tv_shows");
        assertNotNull(result);
        GsonBuilder b = new GsonBuilder();
        Gson g = b.create();
        Type type = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> f = g.fromJson(result, type);
        assertNotNull(f);
        System.out.println(result);

        ArrayList<AudioFile> vv = new ArrayList<>();

        Movie v0 = f.get(322);

//        Movie v0 = f.get(164);
//        Movie v1 = f.get(165);
//        Movie v2 = f.get(166);

        vv.addAll(v0.getFiles());

//        vv.addAll(v1.getFiles());
//        vv.addAll(v2.getFiles());

        for (AudioFile ff : vv) {
            ff.setTitle(v0.getTitle());
            ff.setAnnounce(false);
        }

        String request = g.toJson(vv);
        System.out.println(request);

        String rr = UrlUtility.exchangeJson("http://192.168.100.50:21122/video", request);
        System.out.println(rr);
    }

    //@Test
    public void exchangeTvShowsJson() {
        String result = UrlUtility.grabJson("http://192.168.100.50:21124/tv_shows");
        assertNotNull(result);
        GsonBuilder b = new GsonBuilder();
        Gson g = b.create();
        Type type = new TypeToken<List<TvShow>>(){}.getType();
        List<TvShow> f = g.fromJson(result, type);
        assertNotNull(f);

        ArrayList<AudioFile> vv = new ArrayList<>();
        TvShow v = f.get(165);

        vv.addAll(v.getFiles());

        for (AudioFile ff : vv) {
            ff.setAnnounce(false);
        }
        String request = g.toJson(vv);
        System.out.println(request);

        String rr = UrlUtility.exchangeJson("http://192.168.100.50:21122/video", request);
        System.out.println(rr);
    }

    //@Test
    public void sendQuitJson() {
        String request = "{\"CMD\": \"QUIT\"}";
        String rr = UrlUtility.exchangeJson("http://192.168.100.50:21223/movies/cmd", request);
        System.out.println(rr);
    }
}