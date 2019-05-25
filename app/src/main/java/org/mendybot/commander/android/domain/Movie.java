package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Movie implements Comparable<Movie> {
    private ArrayList<MediaFile> files = new ArrayList<>();
    private UUID uuid;
    private String title;
    private String sortTitle;

    public Movie(UUID uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public UUID getUuid() {
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
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Movie) {
            return uuid.equals(((Movie)o).uuid);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull Movie movie) {
        String check1;
        String check2;
        if (sortTitle != null) {
            check1 = sortTitle;
        } else {
            check1 = title;
        }
        if (movie.sortTitle != null) {
            check2 = movie.sortTitle;
        } else {
            check2 = movie.title;
        }
        return check1.compareTo(check2);
    }

}
