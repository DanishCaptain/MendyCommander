package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TvEpisode implements Comparable<TvEpisode> {
    private ArrayList<MediaFile> fileL = new ArrayList<>();
    private final UUID uuid;
    private final TvSeason season;
    private String title;

    public TvEpisode(TvSeason season, UUID uuid) {
        this.season = season;
        this.uuid = uuid;
    }

    public TvSeason getSeason() {
        return season;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void add(MediaFile f) {
        fileL.add(f);
    }

    @Override
    public int compareTo(@NonNull TvEpisode e) {
        return title.compareTo(e.title);
    }

    public List<MediaFile> getFiles() {
        return fileL;
    }

}
