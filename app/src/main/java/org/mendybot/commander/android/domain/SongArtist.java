package org.mendybot.commander.android.domain;

import java.util.HashMap;

public class SongArtist {
    private HashMap<String, Albumn> albumnM = new HashMap<>();
    private final String name;

    public SongArtist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Albumn lookupAlbumn(String name) {
        Albumn a = albumnM.get(name);
        if (a == null) {
            a = new Albumn(this, name);
            albumnM.put(name, a);
        }
        return a;
    }

}
