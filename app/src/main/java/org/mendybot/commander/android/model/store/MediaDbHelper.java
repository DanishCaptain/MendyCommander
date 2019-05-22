package org.mendybot.commander.android.model.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MediaDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mendybot_media.db";
    private static final int DATABASE_VERSION = 1;

    public MediaDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE[] = {
                "CREATE TABLE " + MovieTable.TABLE_NAME + " (" +
                        MovieTable.COLUMN_UUID + " TEXT PRIMARY KEY, " +
                        MovieTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                        " UNIQUE (" + MovieTable.COLUMN_UUID + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + TvSeriesTable.TABLE_NAME + " (" +
                        TvSeriesTable.COLUMN_TITLE + " TEXT PRIMARY KEY, " +
                        " UNIQUE (" + TvSeriesTable.COLUMN_TITLE + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + TvSeasonTable.TABLE_NAME + " (" +
                        TvSeasonTable.COLUMN_UUID + " TEXT PRIMARY KEY, " +
                        TvSeasonTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                        TvSeasonTable.COLUMN_SERIES_TITLE + " TEXT NOT NULL, " +
                        " UNIQUE (" + TvSeasonTable.COLUMN_UUID + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + TvEpisodeTable.TABLE_NAME + " (" +
                        TvEpisodeTable.COLUMN_UUID + " TEXT PRIMARY KEY, " +
                        TvEpisodeTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                        TvEpisodeTable.COLUMN_SEASON_UUID + " TEXT NOT NULL, " +
                        " UNIQUE (" + TvEpisodeTable.COLUMN_UUID + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + SongArtistTable.TABLE_NAME + " (" +
                        SongArtistTable.COLUMN_NAME + " TEXT PRIMARY KEY, " +
                        " UNIQUE (" + SongArtistTable.COLUMN_NAME + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + SongAlbumTable.TABLE_NAME + " (" +
                        SongAlbumTable.COLUMN_TITLE + " TEXT PRIMARY KEY, " +
                        SongAlbumTable.COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                        " UNIQUE (" + SongAlbumTable.COLUMN_TITLE + ") ON CONFLICT REPLACE" +
                        ");",
                "CREATE TABLE " + SongTrackTable.TABLE_NAME + " (" +
                        SongTrackTable.COLUMN_UUID + " TEXT PRIMARY KEY, " +
                        SongTrackTable.COLUMN_TITLE + " TEXT NOT NULL, " +
                        SongTrackTable.COLUMN_SEASON_UUID + " TEXT NOT NULL, " +
                        " UNIQUE (" + SongTrackTable.COLUMN_UUID + ") ON CONFLICT REPLACE" +
                        ");",
        };
        for (String sql : SQL_CREATE_TABLE) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TvSeriesTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TvSeasonTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TvEpisodeTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongArtistTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongAlbumTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongTrackTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
