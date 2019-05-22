package org.mendybot.commander.android.domain;

import java.util.UUID;

public class MediaFile {
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

}
