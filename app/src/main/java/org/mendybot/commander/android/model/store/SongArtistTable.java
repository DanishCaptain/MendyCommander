package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.SongArtist;

import java.util.List;

public final class SongArtistTable {
    public static final String TABLE_NAME = "music_artist";

    public static final String COLUMN_NAME="name";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_music_artist";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private SongArtistTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<SongArtist> a) {
        ContentValues[] values = new ContentValues[a.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(a.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(SongArtist a) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, a.getName());
        return values;
    }

}
