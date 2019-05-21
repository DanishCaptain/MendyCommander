package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SongTrack implements Comparable<SongTrack> {
    private final UUID id;
    private String name;
    private ArrayList<AudioFile> fileL = new ArrayList<>();

    public SongTrack(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(AudioFile f) {
        fileL.add(f);
    }

    @Override
    public int compareTo(@NonNull SongTrack songTrack) {
        return name.compareTo(songTrack.name);
    }

    public List<AudioFile> getFiles() {
        return fileL;
    }
}
