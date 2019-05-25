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
import org.mendybot.commander.android.model.list.AlbumListListener;
import org.mendybot.commander.android.model.list.MovieListListener;
import org.mendybot.commander.android.model.list.TrackListListener;
import org.mendybot.commander.android.model.list.TvSeasonListListener;
import org.mendybot.commander.android.model.list.TvEpisodeListListener;
import org.mendybot.commander.android.model.store.MediaTable;
import org.mendybot.commander.android.tools.UrlUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final String KEY_AUDIO_BOOK = "AUDIO_BOOK";
    private static MediaModel singleton;
    private ListListenerManager<MovieListListener> movieListManager = new ListListenerManager<>();
    private ListListenerManager<TvSeasonListListener> tvSeasonListManager = new ListListenerManager<>();
    private ListListenerManager<TvEpisodeListListener> tvEpisodeListManager = new ListListenerManager<>();
    private ListListenerManager<AlbumListListener> albumListManager = new ListListenerManager<>();
    private ListListenerManager<TrackListListener> trackListManager = new ListListenerManager<>();
    private ListListenerManager<AlbumListListener> abAlbumListManager = new ListListenerManager<>();
    private ListListenerManager<TrackListListener> abTrackListManager = new ListListenerManager<>();
    private List<Movie> movieL = new ArrayList<>();
    private Map<String, TvSeries> seriesM = new HashMap<>();
    private List<TvSeason> seasonL = new ArrayList<>();

    private Map<String, SongArtist> artistM = new HashMap<>();
    private List<SongAlbum> albumL = new ArrayList<>();
    private Map<String, SongArtist> abArtistM = new HashMap<>();
    private List<SongAlbum> abAlbumL = new ArrayList<>();

    private boolean initialized;
    private ContentResolver resolver;
    private SongAlbum activeAlbum;
    private SongTrack activeTrack;
    private SongAlbum abActiveAlbum;
    private SongTrack abActiveTrack;
    private TvSeason activeSeason;
    private TvEpisode activeEpisode;

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
        Log.d(TAG, "init Media from db");
        Cursor c = resolver.query(MediaTable.CONTENT_URI, null, null, null, null);
        if (c != null) {
            int count = c.getCount();
            Log.d(TAG, "db count "+count);
            for (int i=-0; i<count; i++) {
                c.moveToPosition(i);

                String key = c.getString(c.getColumnIndex(COLUMN_KEY));
                String json = c.getString(c.getColumnIndex(COLUMN_VALUE));

                GsonBuilder b = new GsonBuilder();
                final Gson g = b.create();
                if (KEY_MOVIE.equals(key)) {
                    Log.d(TAG, "load movies from db");
                    Type type = new TypeToken<List<Movie>>(){}.getType();
                    List<Movie> f = g.fromJson(json, type);
                    movieL.clear();
                    movieL.addAll(f);
                    Collections.sort(movieL);
                    movieListManager.notifyAllChanged();
                } else if (KEY_TV_SHOW.equals(key)) {
                    Log.d(TAG, "load tv shows from db");
                    Type type = new TypeToken<List<TvShowInput>>(){}.getType();
                    List<TvShowInput> shows = g.fromJson(json, type);
                    seriesM.clear();
                    seasonL.clear();
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
                        Collections.sort(season.getEpisodes());
                    }
                    Collections.sort(seasonL);
                    tvSeasonListManager.notifyAllChanged();
                    tvEpisodeListManager.notifyAllChanged();
                } else if (KEY_MUSIC.equals(key)) {
                    Log.d(TAG, "load music from db");
                    Type type = new TypeToken<List<SongInput>>(){}.getType();
                    List<SongInput> songs = g.fromJson(json, type);
                    artistM.clear();
                    albumL.clear();
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
                    Collections.sort(albumL);
                    albumListManager.notifyAllChanged();
                    trackListManager.notifyAllChanged();
                } else if (KEY_AUDIO_BOOK.equals(key)) {
                    Log.d(TAG, "load audio books from db");
                    Type type = new TypeToken<List<SongInput>>(){}.getType();
                    List<SongInput> songs = g.fromJson(json, type);
                    abArtistM.clear();
                    abAlbumL.clear();
                    for (SongInput song : songs) {
                        String artistName = song.getArtist();
                        SongArtist artist = abArtistM.get(artistName);
                        if (artist == null) {
                            artist = new SongArtist(artistName);
                            abArtistM.put(artistName, artist);
                        }
                        UUID albumUuid = UUID.fromString(song.getUuid());
                        SongAlbum album = artist.lookupAlbum(albumUuid);
                        album.setTitle(song.getAlbum());
                        if (!abAlbumL.contains(album)) {
                            abAlbumL.add(album);
                        }

                        List<MediaFile> files = song.getFiles();
                        for (MediaFile file : files) {
                            UUID trackUuid = UUID.fromString(file.getUuid());
                            SongTrack track = album.lookup(trackUuid);
                            track.setTitle(file.getTitle());
                            track.add(file);
                        }
                    }
                    Collections.sort(abAlbumL);
                    abAlbumListManager.notifyAllChanged();
                    abTrackListManager.notifyAllChanged();
                }
            }
            c.close();
        }
    }

    public void reloadData() {
        Log.d(TAG, "reload data");
        try {

            new Thread() {
                @Override
                public void run() {
                    Log.d(TAG, "reload data - starting");
                    try {
                        Log.d(TAG, "clear db");
                        resolver.delete(
                                MediaTable.CONTENT_URI,
                                null,
                                null);
                        Log.d(TAG, "db cleared");
                    } catch (Exception e) {
                        Log.w(TAG, e);
                    }
                    GsonBuilder b = new GsonBuilder();
                    final Gson g = b.create();
                    Thread t = initMovies(g);
                    join(t);
                    t = initTvShows(g);
                    join(t);
                    t = initMusic(g);
                    join(t);
                    t = initAudioBook(g);
                    join(t);

                    initMedia(resolver);
                }

                private void join(Thread t) {
                    try {
                        if (t != null) {
                            t.join();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
   }

    private Thread initMovies(final Gson g) {
        final Type type = new TypeToken<List<Movie>>(){}.getType();
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/movies");
                    List<Movie> f = g.fromJson(result, type);
                    if (f.size() > 0) {
                        Log.d(TAG, "init movies db");
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_MOVIE);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            };
            t.start();
            return t;
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
    }

    private Thread initTvShows(final Gson g) {
        final Type type = new TypeToken<List<TvShowInput>>(){}.getType();
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21122/tv_shows");
                    List<TvShowInput> shows = g.fromJson(result, type);
                    if (shows.size() > 0) {
                        Log.d(TAG, "init tv shows db");
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_TV_SHOW);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            };
            t.start();
            return t;
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
    }

    private Thread initMusic(final Gson g) {
        final Type type = new TypeToken<List<SongInput>>(){}.getType();
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21121/music");
                    List<SongInput> songs = g.fromJson(result, type);
                    if (songs.size() > 0) {
                        Log.d(TAG, "init music db");
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_MUSIC);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            };
            t.start();
            return t;
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
        }
    }

    private Thread initAudioBook(final Gson g) {
        final Type type = new TypeToken<List<SongInput>>(){}.getType();
        try {
            Thread t = new Thread() {
                @Override
                public void run() {
                    String result = UrlUtility.grabJson("http://192.168.100.50:21121/audio_book");
                    List<SongInput> songs = g.fromJson(result, type);
                    if (songs.size() > 0) {
                        Log.d(TAG, "init audio books db");
                        ContentValues value = new ContentValues();
                        value.put(COLUMN_KEY, KEY_AUDIO_BOOK);
                        value.put(COLUMN_VALUE, result);
                        resolver.bulkInsert(
                                MediaTable.CONTENT_URI,
                                new ContentValues[]{value});
                    }
                }
            };
            t.start();
            return t;
        } catch (Exception e) {
            Log.w(TAG, e);
            return null;
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

    public List<SongAlbum> getAbAlbums() {
        return abAlbumL;
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

    public void setActiveAbAlbum(SongAlbum album) {
        this.abActiveAlbum = album;
    }

    public SongAlbum getActiveAbAlbum() {
        return abActiveAlbum;
    }

    public void setActiveAbTrack(SongTrack track) {
        this.abActiveTrack = track;
    }

    public SongTrack getActiveAbTrack() {
        return abActiveTrack;
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

    public void addMovieListListener(MovieListListener listListener) {
        movieListManager.addListener(listListener);
    }

    public void removeMovieListListener(MovieListListener listListener) {
        movieListManager.removeListener(listListener);
    }

    public void addTvSeasonListListener(TvSeasonListListener listListener) {
        tvSeasonListManager.addListener(listListener);
    }

    public void removeTvSeasonListListener(TvSeasonListListener listListener) {
        tvSeasonListManager.removeListener(listListener);
    }

    public void addTvEpisodeListListener(TvEpisodeListListener listListener) {
        tvEpisodeListManager.addListener(listListener);
    }

    public void removeTvEpisodeListListener(TvEpisodeListListener listListener) {
        tvEpisodeListManager.removeListener(listListener);
    }

    public void addAlbumListListener(AlbumListListener listListener) {
        albumListManager.addListener(listListener);
    }

    public void removeAlbumListListener(AlbumListListener listListener) {
        albumListManager.removeListener(listListener);
    }

    public void addTrackListListener(TrackListListener listListener) {
        trackListManager.addListener(listListener);
    }

    public void removeTrackListListener(TrackListListener listListener) {
        trackListManager.removeListener(listListener);
    }

    public void addAbAlbumListListener(AlbumListListener listListener) {
        abAlbumListManager.addListener(listListener);
    }

    public void removeAbAlbumListListener(AlbumListListener listListener) {
        abAlbumListManager.removeListener(listListener);
    }

    public void addAbTrackListListener(TrackListListener listListener) {
        abTrackListManager.addListener(listListener);
    }

    public void removeAbTrackListListener(TrackListListener listListener) {
        abTrackListManager.removeListener(listListener);
    }

}
