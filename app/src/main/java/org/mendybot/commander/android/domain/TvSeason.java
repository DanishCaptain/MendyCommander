package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TvSeason implements Comparable<TvSeason> {
    private final TvSeries series;
    private final UUID uuid;
    private ArrayList<TvEpisode> episodeL = new ArrayList<>();
    private HashMap<UUID, TvEpisode> episodeM = new HashMap<>();
    private String name;

    public TvSeason(TvSeries series, UUID uuid) {
        this.series = series;
        this.uuid = uuid;
    }

    public TvSeries getSeries() {
        return series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TvEpisode> getEpisodes() {
        return episodeL;
    }

    public TvEpisode lookup(UUID id) {
        TvEpisode t = episodeM.get(id);
        if (t == null) {
            t = new TvEpisode(this, id);
            episodeM.put(id, t);
            episodeL.add(t);
        }
        return t;
    }

    private String getLongName() {
        return series.getName()+"::"+name;
    }

    @Override
    public int compareTo(@NonNull TvSeason albumn) {
        return getLongName().compareTo(albumn.getLongName());
    }

}
