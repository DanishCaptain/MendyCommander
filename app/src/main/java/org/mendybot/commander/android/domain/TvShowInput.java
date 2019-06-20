package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TvShowInput implements Comparable<TvShowInput> {
    private ArrayList<MediaFile> files = new ArrayList<>();
    private String uuid;
    private String title;
    private String sortTitle;
    private int season;

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public int getSeason() {
        return season;
    }

    public List<MediaFile> getFiles() {
        return files;
    }

    public void add(MediaFile audioFile) {
        files.add(audioFile);
    }

    public void addAll(List<MediaFile> audioFiles) {
        files.addAll(audioFiles);
    }

    @Override
    public int compareTo(@NonNull TvShowInput tvShow) {
        return title.compareTo(tvShow.title);
    }
}
