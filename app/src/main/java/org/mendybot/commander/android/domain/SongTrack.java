package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SongTrack implements Comparable<SongTrack> {
    private ArrayList<MediaFile> fileL = new ArrayList<>();
    private final UUID id;
    private final Albumn albumn;
    private String name;

    public SongTrack(Albumn albumn, UUID id) {
        this.albumn = albumn;
        this.id = id;
    }

    public Albumn getAlbumn() {
        return albumn;
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
    public int compareTo(@NonNull SongTrack songTrack) {
        return name.compareTo(songTrack.name);
    }

    public List<MediaFile> getFiles() {
        return fileL;
    }

}
