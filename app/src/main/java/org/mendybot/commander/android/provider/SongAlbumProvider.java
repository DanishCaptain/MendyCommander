package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.SongAlbumTable;

public class SongAlbumProvider extends MediaProvider {
    private MediaDbHelper helper;

    public SongAlbumProvider() {
        super(SongAlbumTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+SongAlbumTable.COLUMN_TITLE+" DESC";
    }

}
