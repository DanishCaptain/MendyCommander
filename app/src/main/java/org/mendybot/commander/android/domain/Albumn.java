package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Albumn implements Comparable<Albumn> {
    private final SongArtist artist;
    private final String name;
    private ArrayList<SongTrack> trackL = new ArrayList<>();
    private HashMap<UUID, SongTrack> trackM = new HashMap<>();

    public Albumn(SongArtist artist, String name) {
        this.artist = artist;
        this.name = name;
    }

    public SongArtist getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public List<SongTrack> getTracks() {
        return trackL;
    }

    public SongTrack lookup(UUID id) {
        SongTrack t = trackM.get(id);
        if (t == null) {
            t = new SongTrack(id);
            trackM.put(id, t);
            trackL.add(t);
        }
        return t;
    }

    private String getLongName() {
        return artist.getName()+"::"+name;
    }

    @Override
    public int compareTo(@NonNull Albumn albumn) {
        return getLongName().compareTo(albumn.getLongName());
    }

}
