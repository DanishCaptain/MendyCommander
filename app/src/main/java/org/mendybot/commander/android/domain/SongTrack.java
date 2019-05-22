package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SongTrack implements Comparable<SongTrack> {
    private ArrayList<MediaFile> fileL = new ArrayList<>();
    private final UUID uuid;
    private final SongAlbum albumn;
    private String title;

    public SongTrack(SongAlbum albumn, UUID uuid) {
        this.albumn = albumn;
        this.uuid = uuid;
    }

    public SongAlbum getAlbumn() {
        return albumn;
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
    public int compareTo(@NonNull SongTrack songTrack) {
        return title.compareTo(songTrack.title);
    }

    public List<MediaFile> getFiles() {
        return fileL;
    }

}
