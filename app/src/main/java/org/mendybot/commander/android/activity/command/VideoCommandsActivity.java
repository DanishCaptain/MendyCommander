package org.mendybot.commander.android.activity.command;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.tools.UrlUtility;

public class VideoCommandsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = VideoCommandsActivity.class.getSimpleName();
    private View bSendVideoEnd;
    private View bSendVideoPause;
    private View bSendVideoSubtitles;
    private View bSendVideoVolUp;
    private View bSendVideoVolDown;
    private View bSendVideoPrevChapter;
    private View bSendVideoNextChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_commands);

        bSendVideoEnd = findViewById(R.id.send_video_quit);
        bSendVideoEnd.setOnClickListener(this);
        bSendVideoPause = findViewById(R.id.send_video_pause);
        bSendVideoPause.setOnClickListener(this);
        bSendVideoSubtitles = findViewById(R.id.send_video_subtitles);
        bSendVideoSubtitles.setOnClickListener(this);
        bSendVideoVolUp = findViewById(R.id.send_video_vol_up);
        bSendVideoVolUp.setOnClickListener(this);
        bSendVideoVolDown = findViewById(R.id.send_video_vol_down);
        bSendVideoVolDown.setOnClickListener(this);
        bSendVideoPrevChapter = findViewById(R.id.send_video_chap_prev);
        bSendVideoPrevChapter.setOnClickListener(this);
        bSendVideoNextChapter = findViewById(R.id.send_video_chap_next);
        bSendVideoNextChapter.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bSendVideoEnd) {
            sendVideoEnd();
        } else if (view == bSendVideoPause) {
            sendVideoPause();
        } else if (view == bSendVideoSubtitles) {
            sendVideoSubtitles();
        } else if (view == bSendVideoVolUp) {
            sendVideoVolUp();
        } else if (view == bSendVideoVolDown) {
            sendVideoVolDown();
        } else if (view == bSendVideoPrevChapter) {
            sendVideoPrevChapter();
        } else if (view == bSendVideoNextChapter) {
            sendVideoNextChapter();
        }
    }

    private void sendVideoEnd() {
        sendVideoEnd("{\"CMD\": \"QUIT\"}");
    }

    private void sendVideoPause() {
        sendVideoEnd("{\"CMD\": \"PAUSE\"}");
    }

    private void sendVideoSubtitles() {
        sendVideoEnd("{\"CMD\": \"SUBTITLES\"}");
    }

    private void sendVideoVolUp() {
        sendVideoEnd("{\"CMD\": \"VOL_UP\"}");
    }

    private void sendVideoVolDown() {
        sendVideoEnd("{\"CMD\": \"VOL_DOWN\"}");
    }

    private void sendVideoPrevChapter() {
        sendVideoEnd("{\"CMD\": \"PREV_CHAPTER\"}");
    }

    private void sendVideoNextChapter() {
        sendVideoEnd("{\"CMD\": \"NEXT_CHAPTER\"}");
    }

    private void sendVideoEnd(final String request) {

        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://192.168.100.50:21122/movies/cmd", request);
                Log.d(TAG, "result: "+rr);
            }
        }.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
