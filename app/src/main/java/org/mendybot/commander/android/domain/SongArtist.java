package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.UUID;

public class SongArtist implements Comparable<SongArtist> {
    private HashMap<UUID, SongAlbum> albumM = new HashMap<>();
    private final String name;

    public SongArtist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SongAlbum lookupAlbum(UUID uuid) {
        SongAlbum a = albumM.get(uuid);
        if (a == null) {
            a = new SongAlbum(this, uuid);
            albumM.put(uuid, a);
        }
        return a;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SongArtist) {
            return name.equals(((SongArtist)o).name);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull SongArtist artist) {
        return name.compareTo(artist.name);
    }

}
