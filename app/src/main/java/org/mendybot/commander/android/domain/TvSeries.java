package org.mendybot.commander.android.domain;

import java.util.HashMap;
import java.util.UUID;

public class TvSeries {
    private HashMap<UUID, TvSeason> seasonM = new HashMap<>();
    private final String name;

    public TvSeries(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
