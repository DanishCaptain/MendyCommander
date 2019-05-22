package org.mendybot.commander.android.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.mendybot.commander.android.domain.Albumn;
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.SongArtist;
import org.mendybot.commander.android.domain.SongInput;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.domain.TvSeries;
import org.mendybot.commander.android.domain.TvShowInput;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MediaModel {
    private static MediaModel singleton;
    private List<Movie> movieL = new ArrayList<>();
    private Map<String, TvSeries> seriesM = new HashMap<>();
    private List<TvSeason> seasonL = new ArrayList<>();
    private Map<String, SongArtist> artistM = new HashMap<>();
    private List<Albumn> albumnL = new ArrayList<>();
    private Albumn activeAlbumn;
    private SongTrack activeTrack;
    private TvSeason activeSeason;
    private TvEpisode activeEpisode;

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
        MediaFile f = new MediaFile(UUID.randomUUID().toString(), "foobar");
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
        final Type type = new TypeToken<List<TvShowInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21124/tv_shows");
                    List<TvShowInput> shows = g.fromJson(result, type);
                    for (TvShowInput show : shows) {
                        UUID uuid = UUID.fromString(show.getUuid());
                        String title = show.getTitle();
                        String seasonName = "Season "+fillSeason(show.getSeason());
                        TvSeries series = seriesM.get(title);
                        if (series == null) {
                            series = new TvSeries(title);
                            seriesM.put(title, series);
                        }
                        TvSeason season = series.lookupSeason(uuid);
                        if (!seasonL.contains(season)) {
                            seasonL.add(season);
                        }
                        season.setName(seasonName);
                        List<MediaFile> files = show.getFiles();
                        for (MediaFile f : files) {
                            UUID trackId = UUID.fromString(f.getUuid());
                            String episodeName = f.getTitle();
                            TvEpisode episode = season.lookup(trackId);
                            episode.setName(episodeName);
                            episode.add(f);
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fillSeason(int season) {
        if (season < 10) {
            return "00"+season;
        } else if (season < 100) {
            return "0"+season;
        } else {
            return Integer.toString(season);
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
                        List<MediaFile> files = song.getFiles();
                        for (MediaFile f : files) {
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

    public List<TvSeason> getTvSeasons() {
        return seasonL;
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

    public void setActiveAlbumn(Albumn albumn) {
        this.activeAlbumn = albumn;
    }

    public Albumn getActiveAlbumn() {
        return activeAlbumn;
    }

    public void setActiveTrack(SongTrack track) {
        this.activeTrack = track;
    }

    public SongTrack getActiveTrack() {
        return activeTrack;
    }

    public void setActiveSeason(TvSeason season) {
        this.activeSeason = season;
    }

    public TvSeason getActiveSeason() {
        return activeSeason;
    }

    public void setActiveEpisode(TvEpisode episode) {
        this.activeEpisode = episode;
    }

    public TvEpisode getActiveEpisode() {
        return activeEpisode;
    }

}
