package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable<Movie> {
    private ArrayList<MediaFile> files = new ArrayList<>();
    private String uuid;
    private String title;

    public Movie(String uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
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
    public int compareTo(@NonNull Movie movie) {
        return title.compareTo(movie.title);
    }
}
