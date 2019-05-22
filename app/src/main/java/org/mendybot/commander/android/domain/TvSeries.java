package org.mendybot.commander.android.domain;

import java.util.HashMap;
import java.util.UUID;

public class TvSeries {
    private HashMap<UUID, TvSeason> seasonM = new HashMap<>();
    private final String title;

    public TvSeries(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public TvSeason lookupSeason(UUID uuid) {
        TvSeason a = seasonM.get(uuid);
        if (a == null) {
            a = new TvSeason(this, uuid);
            seasonM.put(uuid, a);
        }
        return a;
    }

}
