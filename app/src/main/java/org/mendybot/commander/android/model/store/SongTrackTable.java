package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.SongTrack;

import java.util.List;

public final class SongTrackTable {
    public static final String TABLE_NAME = "music_track";

    public static final String COLUMN_UUID="uuid";
    public static final String COLUMN_ALBUM_TITLE="album_title";
    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_music_track";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private SongTrackTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<SongTrack> t) {
        ContentValues[] values = new ContentValues[t.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(t.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(SongTrack t) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UUID, t.getUuid().toString());
        values.put(COLUMN_ALBUM_TITLE, t.getAlbumn().getTitle());
        values.put(COLUMN_TITLE, t.getTitle());
        return values;
    }

}
