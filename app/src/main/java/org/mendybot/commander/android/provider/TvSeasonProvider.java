package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.TvSeasonTable;

public class TvSeasonProvider extends MediaProvider {
    private MediaDbHelper helper;

    public TvSeasonProvider() {
        super(TvSeasonTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+TvSeasonTable.COLUMN_TITLE+" DESC";
    }

}
