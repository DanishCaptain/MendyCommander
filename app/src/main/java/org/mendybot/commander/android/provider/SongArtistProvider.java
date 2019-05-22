package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.SongArtistTable;

public class SongArtistProvider extends MediaProvider {
    private MediaDbHelper helper;

    public SongArtistProvider() {
        super(SongArtistTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+SongArtistTable.COLUMN_NAME+" DESC";
    }

}
