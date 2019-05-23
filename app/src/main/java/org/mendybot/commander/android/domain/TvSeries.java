package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class TvSeries implements Comparable<TvSeries> {
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

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TvSeries) {
            return title.equals(((TvSeries)o).title);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull TvSeries series) {
        return title.compareTo(series.title);
    }

}
