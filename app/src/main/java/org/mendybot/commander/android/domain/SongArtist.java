package org.mendybot.commander.android.domain;

import java.util.HashMap;
import java.util.UUID;

public class SongArtist {
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

}
