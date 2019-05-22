package org.mendybot.commander.android.provider;

import org.mendybot.commander.android.model.store.MovieTable;

public class MovieProvider extends MediaProvider {

    public MovieProvider() {
        super(MovieTable.TABLE_NAME);
    }

    @Override
    protected String getOrderBy() {
        return "order by "+MovieTable.COLUMN_TITLE+" DESC";
    }

}
