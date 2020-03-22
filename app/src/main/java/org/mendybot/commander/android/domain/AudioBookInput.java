package org.mendybot.commander.android.domain;

import java.util.ArrayList;
import java.util.List;

public class AudioBookInput {
    private List<MediaFile> files = new ArrayList<>();
    private String uuid;
    private String title;
    private String sortTitle;
    private String author;

    public String getUuid() {
        return uuid;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public List<MediaFile> getFiles() {
        return files;
    }
}
