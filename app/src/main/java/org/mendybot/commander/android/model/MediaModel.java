package org.mendybot.commander.android.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.mendybot.commander.android.domain.AudioFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.TvShow;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class MediaModel {
    private static MediaModel singleton;
    private List<Movie> movies = new ArrayList<>();
    private List<TvShow> tvShows = new ArrayList<>();

    private MediaModel() {
    }

    public void init(Context context) {
        GsonBuilder b = new GsonBuilder();
        final Gson g = b.create();
        initMovies(g);
        initTvShows(g);
    }

    private void initMovies(final Gson g) {
        final Type type = new TypeToken<List<Movie>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21123/movies");
                    List<Movie> f = g.fromJson(result, type);
                    movies.addAll(f);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTvShows(final Gson g) {
        final Type type = new TypeToken<List<TvShow>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21124/tv_shows");
                    List<TvShow> shows = g.fromJson(result, type);
                    for (TvShow tv : shows) {
                        List<AudioFile> files = tv.getFiles();
                        for (AudioFile f : files) {
                            TvShow show = new TvShow(tv.getUuid(), f.getTitle(), tv.getSeason());
                            show.add(f);
                            tvShows.add(show);
                        }
                    }
//                    tvShows.addAll(f);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<TvShow> getTvShows() {
        return tvShows;
    }

    public synchronized static MediaModel getInstance() {
        if (singleton == null) {
            singleton =  new MediaModel();
        }
        return singleton;
    }

}
