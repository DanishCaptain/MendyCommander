package org.mendybot.commander.android.model.store;

import android.content.ContentValues;
import android.net.Uri;

import org.mendybot.commander.android.domain.TvEpisode;

import java.util.List;

public final class TvEpisodeTable {
    public static final String TABLE_NAME = "tv_episode";

    public static final String COLUMN_UUID="uuid";
    public static final String COLUMN_SEASON_UUID="season_uuid";
    public static final String COLUMN_TITLE="title";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_tv_episode";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private TvEpisodeTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

    public static ContentValues[] grabContentValues(List<TvEpisode> episodes) {
        ContentValues[] values = new ContentValues[episodes.size()];
        for (int i=0; i<values.length; i++) {
            values[i] = grabContentValue(episodes.get(i));
        }
        return values;

    }

    private static ContentValues grabContentValue(TvEpisode episode) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UUID, episode.getUuid().toString());
        values.put(COLUMN_SEASON_UUID, episode.getSeason().getUuid().toString());
        values.put(COLUMN_TITLE, episode.getTitle());
        return values;
    }
}
