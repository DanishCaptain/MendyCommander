package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.TvSeriesTable;

public class TvSeriesProvider extends MediaProvider {
    private MediaDbHelper helper;

    public TvSeriesProvider() {
        super(TvSeriesTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+ TvSeriesTable.COLUMN_TITLE+" DESC";
    }

}
