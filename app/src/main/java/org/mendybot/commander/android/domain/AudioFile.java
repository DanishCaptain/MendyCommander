package org.mendybot.commander.android.domain;

public class AudioFile {
    private String title;
    private String fileName;
    private boolean announce;

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
