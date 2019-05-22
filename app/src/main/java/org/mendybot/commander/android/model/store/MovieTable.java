package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.json.JSONObject;
import org.mendybot.commander.android.domain.Movie;

import java.util.List;

public final class MovieTable {
    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_UUID="uuid";
    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_movie";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private MovieTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<Movie> movies) {
        ContentValues[] values = new ContentValues[movies.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(movies.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UUID, movie.getUuid().toString());
        values.put(COLUMN_TITLE, movie.getTitle());
        return values;
    }
}
