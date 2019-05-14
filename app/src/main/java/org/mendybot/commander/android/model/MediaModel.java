package org.mendybot.commander.android.model;

import org.mendybot.commander.android.tools.UrlUtility;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.net.sip.SipErrorCode.TIME_OUT;

public final class MediaModel {
    private static MediaModel singleton;

    private MediaModel() {
    }

    public void doIt() {
        UrlUtility.doIt();
    }

    public synchronized static MediaModel getInstance() {
        if (singleton == null) {
            singleton =  new MediaModel();
        }
        return singleton;
    }
}
