package org.mendybot.commander.android.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.mendybot.commander.android.domain.Albumn;
import org.mendybot.commander.android.domain.AudioFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.SongArtist;
import org.mendybot.commander.android.domain.SongInput;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.domain.TvShow;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MediaModel {
    private static MediaModel singleton;
    private List<Movie> movieL = new ArrayList<>();
    private List<TvShow> tvShowL = new ArrayList<>();
    private Map<String, SongArtist> artistM = new HashMap<>();
    private List<Albumn> albumnL = new ArrayList<>();
    private Albumn activeAlbumn;

    private MediaModel() {
    }

    public void init(Context context) {
        GsonBuilder b = new GsonBuilder();
        final Gson g = b.create();
        initMovies(g);
        initTvShows(g);
        initMusic(g);

        /*
        SongArtist artist = new SongArtist("Rush");
        Albumn albumn = artist.lookupAlbumn("2112");
        albumnL.add(albumn);
        SongTrack track = albumn.lookup(UUID.randomUUID());
        track.setName("Prelude");
        AudioFile f = new AudioFile(UUID.randomUUID().toString(), "foobar");
        track.add(f);
        */
    }

    private void initMovies(final Gson g) {
        final Type type = new TypeToken<List<Movie>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21123/movies");
                    List<Movie> f = g.fromJson(result, type);
                    movieL.addAll(f);
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
                            tvShowL.add(show);
                        }
                    }
//                    tvShows.addAll(f);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMusic(final Gson g) {
        final Type type = new TypeToken<List<SongInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21125/music");
                    List<SongInput> songs = g.fromJson(result, type);
                    for (SongInput song : songs) {
                        String artistName = song.getArtist();
                        SongArtist artist = artistM.get(artistName);
                        if (artist == null) {
                            artist = new SongArtist(artistName);
                            artistM.put(artistName, artist);
                        }
                        String albumnName = song.getAlbumn();
                        Albumn albumn = artist.lookupAlbumn(albumnName);
                        if (!albumnL.contains(albumn)) {
                            albumnL.add(albumn);
                        }
                        List<AudioFile> files = song.getFiles();
                        for (AudioFile f : files) {
//                            UUID trackId = UUID.fromString(song.getUuid());
                            UUID trackId = UUID.fromString(f.getUuid());
                            String trackName = f.getTitle();
                            SongTrack track = albumn.lookup(trackId);
                            track.setName(trackName);
                            track.add(f);
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getMovies() {
        return movieL;
    }

    public List<TvShow> getTvShows() {
        return tvShowL;
    }

    public List<Albumn> getAlbumns() {
        return albumnL;
    }

    public synchronized static MediaModel getInstance() {
        if (singleton == null) {
            singleton =  new MediaModel();
        }
        return singleton;
    }

    public void setActive(Albumn albumn) {
        this.activeAlbumn = albumn;
    }

    public Albumn getActive() {
        return activeAlbumn;
    }
}
