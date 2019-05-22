package org.mendybot.commander.android.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.mendybot.commander.android.domain.SongAlbum;
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.SongArtist;
import org.mendybot.commander.android.domain.SongInput;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.domain.TvSeries;
import org.mendybot.commander.android.domain.TvShowInput;
import org.mendybot.commander.android.model.store.MovieTable;
import org.mendybot.commander.android.model.store.SongAlbumTable;
import org.mendybot.commander.android.model.store.SongArtistTable;
import org.mendybot.commander.android.model.store.SongTrackTable;
import org.mendybot.commander.android.model.store.TvEpisodeTable;
import org.mendybot.commander.android.model.store.TvSeasonTable;
import org.mendybot.commander.android.model.store.TvSeriesTable;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MediaModel {
    private static MediaModel singleton;
    private List<Movie> movieL = new ArrayList<>();
    private Map<String, TvSeries> seriesM = new HashMap<>();
    private Map<UUID, TvSeason> seasonM = new HashMap<>();
    private List<TvSeason> seasonL = new ArrayList<>();
    private Map<String, SongArtist> artistM = new HashMap<>();
    private Map<String, SongAlbum> albumM = new HashMap<>();
    private List<SongAlbum> albumL = new ArrayList<>();

    private boolean initialized;
    private SongAlbum activeAlbum;
    private SongTrack activeTrack;
    private TvSeason activeSeason;
    private TvEpisode activeEpisode;
    private ContentResolver resolver;

    private MediaModel() {
    }

    public void init(Context context) {
        if (!initialized) {
            initialized = true;
        }
        resolver = context.getContentResolver();

        initMovies(resolver);
        initTvShows(resolver);
        initMusic(resolver);
    }

    private void initMovies(ContentResolver resolver) {
        Cursor c = resolver.query(MovieTable.CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            Movie m = new Movie(
                    UUID.fromString(c.getString(c.getColumnIndex(MovieTable.COLUMN_UUID))),
                    c.getString(c.getColumnIndex(MovieTable.COLUMN_TITLE))
            );
            movieL.add(m);
        }
    }

    private void initTvShows(ContentResolver resolver) {
        Cursor c = resolver.query(TvSeriesTable.CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            TvSeries s = new TvSeries(
                    c.getString(c.getColumnIndex(TvSeriesTable.COLUMN_TITLE))
            );
            seriesM.put(s.getTitle(), s);
        }
        c = resolver.query(TvSeasonTable.CONTENT_URI, null, null, null, null);
        count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            String seriesName = c.getString(c.getColumnIndex(TvSeasonTable.COLUMN_SERIES_TITLE));
            if (seriesName == null) {
                seriesName = "nomine";
            }
            TvSeries s = seriesM.get(seriesName);
            if (s == null) {
                s = new TvSeries(seriesName);
                seriesM.put(s.getTitle(), s);
            }
            TvSeason season = s.lookupSeason(UUID.fromString(c.getString(c.getColumnIndex(TvSeasonTable.COLUMN_UUID))));
            season.setTitle(c.getString(c.getColumnIndex(TvSeasonTable.COLUMN_TITLE)));
            seasonM.put(season.getUuid(), season);
            seasonL.add(season);
        }
        c = resolver.query(TvEpisodeTable.CONTENT_URI, null, null, null, null);
        count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            UUID seasonId = UUID.fromString(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_SEASON_UUID)));
            TvSeason season = seasonM.get(seasonId);
            TvEpisode episode = season.lookup(UUID.fromString(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_UUID))));
            episode.setTitle(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_TITLE)));
        }
    }

    private void initMusic(ContentResolver resolver) {
        Cursor c = resolver.query(SongArtistTable.CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            SongArtist s = new SongArtist(c.getString(c.getColumnIndex(SongArtistTable.COLUMN_NAME))
            );
            artistM.put(s.getName(), s);
        }
        c = resolver.query(SongAlbumTable.CONTENT_URI, null, null, null, null);
        count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            String artistName = c.getString(c.getColumnIndex(SongAlbumTable.COLUMN_ARTIST_NAME));
            if (artistName == null) {
                artistName = "nomine";
            }
            SongArtist s = artistM.get(artistName);
            if (s == null) {
                s = new SongArtist(artistName);
                artistM.put(s.getName(), s);
            }
            SongAlbum album = s.lookupAlbum(c.getString(c.getColumnIndex(SongAlbumTable.COLUMN_TITLE)));
            albumM.put(album.getTitle(), album);
            albumL.add(album);
        }
        c = resolver.query(SongTrackTable.CONTENT_URI, null, null, null, null);
        count = c.getCount();
        for (int i=-0; i<count; i++) {
            c.moveToPosition(i);
            UUID seasonId = UUID.fromString(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_SEASON_UUID)));
            TvSeason season = seasonM.get(seasonId);
            TvEpisode episode = season.lookup(UUID.fromString(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_UUID))));
            episode.setTitle(c.getString(c.getColumnIndex(TvEpisodeTable.COLUMN_TITLE)));
        }
    }

    public void reloadData() {
        try {
            resolver.delete(
                    MovieTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    SongAlbumTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    SongArtistTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    SongTrackTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    TvEpisodeTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    TvSeasonTable.CONTENT_URI,
                    null,
                    null);
            resolver.delete(
                    TvSeriesTable.CONTENT_URI,
                    null,
                    null);

            /*
            Movie m = new Movie(UUID.randomUUID(), "Star Wars");
            List<Movie> movies = new ArrayList();
            movies.add(m);

            resolver.bulkInsert(
                    MovieTable.CONTENT_URI,
                    MovieTable.grabContentValues(movies));
                    */
        } catch (Exception e) {
            e.printStackTrace();
        }
        GsonBuilder b = new GsonBuilder();
        final Gson g = b.create();
        initMovies(g);
        initTvShows(g);
        initMusic(g);

    }

    private void initMovies(final Gson g) {
        final List<Movie> list = new ArrayList<>();
        final Type type = new TypeToken<List<Movie>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/movies");
                    List<Movie> f = g.fromJson(result, type);
                    list.addAll(f);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resolver.bulkInsert(
                MovieTable.CONTENT_URI,
                MovieTable.grabContentValues(list));

    }

    private void initTvShows(final Gson g) {
        final List<TvEpisode> episodesL = new ArrayList<>();
        final List<TvSeason> seasonL = new ArrayList<>();
        final List<TvSeries> seriesL = new ArrayList<>();
        final Map<String, TvSeries> seriesM = new HashMap<>();
        final Type type = new TypeToken<List<TvShowInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/tv_shows");
                    List<TvShowInput> shows = g.fromJson(result, type);
                    for (TvShowInput show : shows) {
                        UUID uuid = UUID.fromString(show.getUuid());
                        String title = show.getTitle();
                        String seasonTitle = "Season "+fillSeason(show.getSeason());
                        TvSeries series = seriesM.get(title);
                        if (series == null) {
                            series = new TvSeries(title);
                            seriesM.put(title, series);
                            seriesL.add(series);
                        }
                        TvSeason season = series.lookupSeason(uuid);
                        if (!seasonL.contains(season)) {
                            seasonL.add(season);
                        }
                        season.setTitle(seasonTitle);
                        List<MediaFile> files = show.getFiles();
                        for (MediaFile f : files) {
                            UUID trackId = UUID.fromString(f.getUuid());
                            String episodeTitle = f.getTitle();
                            TvEpisode episode = season.lookup(trackId);
                            episode.setTitle(episodeTitle);
                            episode.add(f);
                            episodesL.add(episode);
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resolver.bulkInsert(
                TvEpisodeTable.CONTENT_URI,
                TvEpisodeTable.grabContentValues(episodesL));
        resolver.bulkInsert(
                TvSeasonTable.CONTENT_URI,
                TvSeasonTable.grabContentValues(seasonL));
        resolver.bulkInsert(
                TvSeriesTable.CONTENT_URI,
                TvSeriesTable.grabContentValues(seriesL));
    }

    private void initMusic(final Gson g) {
        final Map<String, SongArtist> artistM = new HashMap<>();
        final List<SongArtist> artistL = new ArrayList<>();
        final Map<String, SongAlbum> albumM = new HashMap<>();
        final List<SongAlbum> albumL = new ArrayList<>();
        final List<SongTrack> trackL = new ArrayList<>();
        final Type type = new TypeToken<List<SongInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21121/music");
                    List<SongInput> songs = g.fromJson(result, type);
                    for (SongInput song : songs) {
                        String artistName = song.getArtist();
                        SongArtist artist = artistM.get(artistName);
                        if (artist == null) {
                            artist = new SongArtist(artistName);
                            artistM.put(artistName, artist);
                            artistL.add(artist);
                        }
                        String albumName = song.getAlbum();
                        SongAlbum album = artist.lookupAlbum(albumName);
                        if (!albumL.contains(album)) {
                            albumM.put(album.getTitle(), album);
                            albumL.add(album);
                        }
                        List<MediaFile> files = song.getFiles();
                        for (MediaFile f : files) {
                            UUID trackId = UUID.fromString(f.getUuid());
                            String trackTitle = f.getTitle();
                            SongTrack track = album.lookup(trackId);
                            track.setTitle(trackTitle);
                            track.add(f);
                            trackL.add(track);
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resolver.bulkInsert(
                SongAlbumTable.CONTENT_URI,
                SongAlbumTable.grabContentValues(albumL));
        resolver.bulkInsert(
                SongArtistTable.CONTENT_URI,
                SongArtistTable.grabContentValues(artistL));
        resolver.bulkInsert(
                SongTrackTable.CONTENT_URI,
                SongTrackTable.grabContentValues(trackL));
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

    public List<Movie> getMovies() {
        return movieL;
    }

    public List<TvSeason> getTvSeasons() {
        return seasonL;
    }

    public List<SongAlbum> getAlbums() {
        return albumL;
    }

    public synchronized static MediaModel getInstance() {
        if (singleton == null) {
            singleton =  new MediaModel();
        }
        return singleton;
    }

    public void setActiveAlbum(SongAlbum album) {
        this.activeAlbum = album;
    }

    public SongAlbum getActiveAlbum() {
        return activeAlbum;
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
