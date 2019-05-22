package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.SongTrackTable;

public class SongTrackProvider extends MediaProvider {
    private MediaDbHelper helper;

    public SongTrackProvider() {
        super(SongTrackTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+SongTrackTable.COLUMN_TITLE+" DESC";
    }

}
