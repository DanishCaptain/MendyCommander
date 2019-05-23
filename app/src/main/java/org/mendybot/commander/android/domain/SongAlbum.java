package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SongAlbum implements Comparable<SongAlbum> {
    private final SongArtist artist;
    private final UUID uuid;
    private String title;
    private ArrayList<SongTrack> trackL = new ArrayList<>();
    private HashMap<UUID, SongTrack> trackM = new HashMap<>();

    public SongAlbum(SongArtist artist, UUID uuid) {
        this.artist = artist;
        this.uuid = uuid;
    }

    public SongArtist getArtist() {
        return artist;
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

    public List<SongTrack> getTracks() {
        return trackL;
    }

    public SongTrack lookup(UUID id) {
        SongTrack t = trackM.get(id);
        if (t == null) {
            t = new SongTrack(this, id);
            trackM.put(id, t);
            trackL.add(t);
        }
        return t;
    }

    private String getLongName() {
        return artist.getName()+"::"+title;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SongAlbum) {
            return title.equals(((SongAlbum)o).title);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull SongAlbum albumn) {
        return getLongName().compareTo(albumn.getLongName());
    }

}
