package org.mendybot.commander.android.model.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MediaDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mendybot_media.db";
    private static final int DATABASE_VERSION = 2;

    public MediaDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE[] = {
                "CREATE TABLE " + MediaTable.TABLE_NAME + " (" +
                        MediaTable.COLUMN_KEY + " TEXT PRIMARY KEY, " +
                        MediaTable.COLUMN_VALUE + " TEXT NOT NULL, " +
                        " UNIQUE (" + MediaTable.COLUMN_KEY + ") ON CONFLICT REPLACE" +
                        ");",
        };
        for (String sql : SQL_CREATE_TABLE) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MediaTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
