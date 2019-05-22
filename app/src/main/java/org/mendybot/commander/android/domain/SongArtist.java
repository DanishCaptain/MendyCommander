package org.mendybot.commander.android.domain;

import java.util.HashMap;

public class SongArtist {
    private HashMap<String, SongAlbum> albumM = new HashMap<>();
    private final String name;

    public SongArtist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SongAlbum lookupAlbum(String name) {
        SongAlbum a = albumM.get(name);
        if (a == null) {
            a = new SongAlbum(this, name);
            albumM.put(name, a);
        }
        return a;
    }

}
