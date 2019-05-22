package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TvEpisode implements Comparable<TvEpisode> {
    private ArrayList<MediaFile> fileL = new ArrayList<>();
    private final UUID id;
    private final TvSeason season;
    private String name;

    public TvEpisode(TvSeason season, UUID id) {
        this.season = season;
        this.id = id;
    }

    public TvSeason getSeason() {
        return season;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(MediaFile f) {
        fileL.add(f);
    }

    @Override
    public int compareTo(@NonNull TvEpisode e) {
        return name.compareTo(e.name);
    }

    public List<MediaFile> getFiles() {
        return fileL;
    }

}
