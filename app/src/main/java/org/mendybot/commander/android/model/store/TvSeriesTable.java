package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.TvSeries;

import java.util.List;

public final class TvSeriesTable {
    public static final String TABLE_NAME = "tv_series";

    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_MEDIA = "media_tv_series";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MEDIA)
            .build();

    private TvSeriesTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<TvSeries> seriesL) {
        ContentValues[] values = new ContentValues[seriesL.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(seriesL.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(TvSeries series) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, series.getTitle());
        return values;
    }
}
