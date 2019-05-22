package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.domain.TvSeason;

import java.util.List;

public final class TvSeasonTable {
    public static final String TABLE_NAME = "tv_season";

    public static final String COLUMN_UUID="uuid";
    public static final String COLUMN_SERIES_TITLE="series_title";
    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_tv_season";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private TvSeasonTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<TvSeason> seasons) {
        ContentValues[] values = new ContentValues[seasons.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(seasons.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(TvSeason season) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UUID, season.getUuid().toString());
        values.put(COLUMN_SERIES_TITLE, season.getSeries().getTitle());
        values.put(COLUMN_TITLE, season.getTitle());
        return values;
    }
}
