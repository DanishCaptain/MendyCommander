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
    private String title;

    public TvSeason(TvSeries series, UUID uuid) {
        this.series = series;
        this.uuid = uuid;
    }

    public TvSeries getSeries() {
        return series;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    private String getLongTitle() {
        return series.getTitle()+"::"+title;
    }

    @Override
    public int compareTo(@NonNull TvSeason albumn) {
        return getLongTitle().compareTo(albumn.getLongTitle());
    }

}
