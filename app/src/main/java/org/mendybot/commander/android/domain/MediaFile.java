package org.mendybot.commander.android.domain;

import android.support.annotation.NonNull;

public class MediaFile implements Comparable<MediaFile> {
    private String uuid;
    private String title;
    private String fileName;
    private boolean announce;

    public MediaFile(String uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAnnounce(boolean announce) {
        this.announce = announce;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MediaFile) {
            return uuid.equals(((MediaFile)o).uuid);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull MediaFile file) {
        return title.compareTo(file.title);
    }

}
