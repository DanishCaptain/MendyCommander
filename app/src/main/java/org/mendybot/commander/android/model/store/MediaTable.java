package org.mendybot.commander.android.model.store;

import android.net.Uri;

public final class MediaTable {
    public static final String TABLE_NAME = "media_locker";

    public static final String COLUMN_KEY="key";
    public static final String COLUMN_VALUE="value";

    private static final String CONTENT_AUTHORITY = "org.mendybot.commander.android."+TABLE_NAME;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_NEWS = "media_movie";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_NEWS)
            .build();

    private MediaTable() {
    }

    public static String getSqlSelectFilter() {
        return "";
    }

}
