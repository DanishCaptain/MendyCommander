package org.mendybot.commander.android.domain;

import java.util.ArrayList;
import java.util.List;

public class SongInput {
    private ArrayList<MediaFile> files = new ArrayList<>();
    private String uuid;
    private String artist;
    private String albumn;

    public String getUuid() {
        return uuid;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return albumn;
    }

    public List<MediaFile> getFiles() {
        return files;
    }

}
