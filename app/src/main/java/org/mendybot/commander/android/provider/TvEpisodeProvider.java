package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MediaDbHelper;
import org.mendybot.commander.android.model.store.TvEpisodeTable;

public class TvEpisodeProvider extends MediaProvider {
    private MediaDbHelper helper;

    public TvEpisodeProvider() {
        super(TvEpisodeTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+ TvEpisodeTable.COLUMN_TITLE+" DESC";
    }

}
