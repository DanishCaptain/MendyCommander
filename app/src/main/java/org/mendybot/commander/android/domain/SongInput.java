package org.mendybot.commander.android.domain;

import java.util.ArrayList;
import java.util.List;

public class SongInput {
    private ArrayList<AudioFile> files = new ArrayList<>();
    private String title;
    private String uuid;
    private String artist;
    private String albumn;

    public String getUuid() {
        return uuid;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumn() {
        return albumn;
    }

    public String getTitle() {
        return title;
    }

    public List<AudioFile> getFiles() {
        return files;
    }

}
