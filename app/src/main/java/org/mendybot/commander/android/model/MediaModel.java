package org.mendybot.commander.android.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.SongAlbum;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.SongArtist;
import org.mendybot.commander.android.domain.SongInput;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.domain.TvSeries;
import org.mendybot.commander.android.domain.TvShowInput;
import org.mendybot.commander.android.model.store.MediaTable;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mendybot.commander.android.model.store.MediaTable.COLUMN_KEY;
import static org.mendybot.commander.android.model.store.MediaTable.COLUMN_VALUE;

public final class MediaModel {
    private static final String TAG = MediaModel.class.getSimpleName();
    private static final String KEY_MOVIE = "MOVIE";
    private static final String KEY_TV_SHOW = "TV_SHOW";
    private static final String KEY_MUSIC = "MUSIC";
    private static MediaModel singleton;
    private List<Movie> movieL = new ArrayList<>();
    private Map<String, TvSeries> seriesM = new HashMap<>();
    private List<TvSeason> seasonL = new ArrayList<>();
    private Map<String, SongArtist> artistM = new HashMap<>();
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

        initMedia(resolver);
    }

    private void initMedia(ContentResolver resolver) {
        Cursor c = resolver.query(MediaTable.CONTENT_URI, null, null, null, null);
        if (c != null) {
            int count = c.getCount();
            for (int i=-0; i<count; i++) {
                c.moveToPosition(i);

                String key = c.getString(c.getColumnIndex(COLUMN_KEY));
                String json = c.getString(c.getColumnIndex(COLUMN_VALUE));

                GsonBuilder b = new GsonBuilder();
                final Gson g = b.create();
                if (KEY_MOVIE.equals(key)) {
                    Type type = new TypeToken<List<Movie>>(){}.getType();
                    List<Movie> f = g.fromJson(json, type);
                    movieL.addAll(f);

                } else if (KEY_TV_SHOW.equals(key)) {
                    Type type = new TypeToken<List<TvShowInput>>(){}.getType();
                    List<TvShowInput> shows = g.fromJson(json, type);
                    for (TvShowInput tv : shows) {
                        String seriesTitle = tv.getTitle();
                        TvSeries series = seriesM.get(seriesTitle);
                        if (series == null) {
                            series = new TvSeries(seriesTitle);
                            seriesM.put(seriesTitle, series);
                        }
                        UUID seasonUuid = UUID.fromString(tv.getUuid());
                        TvSeason season = series.lookupSeason(seasonUuid);
                        String seasonTitle = "Season "+fillSeason(tv.getSeason());
                        season.setTitle(seasonTitle);
                        if (!seasonL.contains(season)) {
                            seasonL.add(season);
                        }

                        List<MediaFile> files = tv.getFiles();
                        for (MediaFile file : files) {
                            UUID episodeUuid = UUID.fromString(file.getUuid());
                            TvEpisode episode = season.lookup(episodeUuid);
                            episode.setTitle(file.getTitle());
                            episode.add(file);
                        }
                    }
                } else if (KEY_MUSIC.equals(key)) {
                    Type type = new TypeToken<List<SongInput>>(){}.getType();
                    List<SongInput> songs = g.fromJson(json, type);
                    for (SongInput song : songs) {
                        String artistName = song.getArtist();
                        SongArtist artist = artistM.get(artistName);
                        if (artist == null) {
                            artist = new SongArtist(artistName);
                            artistM.put(artistName, artist);
                        }
                        UUID albumUuid = UUID.fromString(song.getUuid());
                        SongAlbum album = artist.lookupAlbum(albumUuid);
                        album.setTitle(song.getAlbum());
                        if (!albumL.contains(album)) {
                            albumL.add(album);
                        }

                        List<MediaFile> files = song.getFiles();
                        for (MediaFile file : files) {
                            UUID trackUuid = UUID.fromString(file.getUuid());
                            SongTrack track = album.lookup(trackUuid);
                            track.setTitle(file.getTitle());
                            track.add(file);
                        }
                    }
                }
            }
            c.close();
        }
    }

    public void reloadData() {
        try {
            resolver.delete(
                    MediaTable.CONTENT_URI,
                    null,
                    null);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        GsonBuilder b = new GsonBuilder();
        final Gson g = b.create();
        initMovies(g);
        initTvShows(g);
        initMusic(g);
    }

    private void initMovies(final Gson g) {
        final Type type = new TypeToken<List<Movie>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/movies");
                    List<Movie> f = g.fromJson(result, type);
                    if (f.size() > 0) {
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_MOVIE);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void initTvShows(final Gson g) {
        final Type type = new TypeToken<List<TvShowInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/tv_shows");
                    List<TvShowInput> shows = g.fromJson(result, type);
                    if (shows.size() > 0) {
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_TV_SHOW);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void initMusic(final Gson g) {
        final Type type = new TypeToken<List<SongInput>>(){}.getType();
        try {
            new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21121/music");
                    List<SongInput> songs = g.fromJson(result, type);
                    if (songs.size() > 0) {
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_MUSIC);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.w(TAG, e);
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
