package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TvShow implements Comparable<TvShow> {
    private ArrayList<AudioFile> files = new ArrayList<>();
    private String uuid;
    private String title;
    private int season;

    public TvShow(String uuid, String title, int season) {
        this.uuid = uuid;
        this.title = title;
        this.season = season;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public int getSeason() {
        return season;
    }

    public List<AudioFile> getFiles() {
        return files;
    }

    public void add(AudioFile audioFile) {
        files.add(audioFile);
    }

    public void addAll(List<AudioFile> audioFiles) {
        files.addAll(audioFiles);
    }

    @Override
    public int compareTo(@NonNull TvShow tvShow) {
        return title.compareTo(tvShow.title);
    }
}
