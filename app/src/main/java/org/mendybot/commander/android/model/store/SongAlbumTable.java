package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.SongAlbum;

import java.util.List;

public final class SongAlbumTable {
    public static final String TABLE_NAME = "music_album";

    public static final String COLUMN_ARTIST_NAME="artist_name";
    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_music_album";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private SongAlbumTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<SongAlbum> a) {
        ContentValues[] values = new ContentValues[a.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(a.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(SongAlbum a) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, a.getTitle());
        values.put(COLUMN_ARTIST_NAME, a.getArtist().getName());
        return values;
    }
}
