package org.mendybot.commander.android.activity.command;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

public class AudioCommandsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AudioCommandsActivity.class.getSimpleName();
    private View bSendAudioEnd;
    private View bSendAudioPause;
    private View bSendAudioSubtitles;
    private View bSendAudioVolUp;
    private View bSendAudioVolDown;
    private View bSendAudioPrevChapter;
    private View bSendAudioNextChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_commands);

        bSendAudioEnd = findViewById(R.id.send_audio_quit);
        bSendAudioEnd.setOnClickListener(this);
        bSendAudioPause = findViewById(R.id.send_audio_pause);
        bSendAudioPause.setOnClickListener(this);
        bSendAudioSubtitles = findViewById(R.id.send_audio_subtitles);
        bSendAudioSubtitles.setOnClickListener(this);
        bSendAudioVolUp = findViewById(R.id.send_audio_vol_up);
        bSendAudioVolUp.setOnClickListener(this);
        bSendAudioVolDown = findViewById(R.id.send_audio_vol_down);
        bSendAudioVolDown.setOnClickListener(this);
        bSendAudioPrevChapter = findViewById(R.id.send_audio_chap_prev);
        bSendAudioPrevChapter.setOnClickListener(this);
        bSendAudioNextChapter = findViewById(R.id.send_audio_chap_next);
        bSendAudioNextChapter.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bSendAudioEnd) {
            sendAudioEnd();
        } else if (view == bSendAudioPause) {
            sendAudioPause();
        } else if (view == bSendAudioSubtitles) {
            sendAudioSubtitles();
        } else if (view == bSendAudioVolUp) {
            sendAudioVolUp();
        } else if (view == bSendAudioVolDown) {
            sendAudioVolDown();
        } else if (view == bSendAudioPrevChapter) {
            sendAudioPrevChapter();
        } else if (view == bSendAudioNextChapter) {
            sendAudioNextChapter();
        }
    }

    private void sendAudioEnd() {
        sendAudioEnd("{\"CMD\": \"QUIT\"}");
    }

    private void sendAudioPause() {
        sendAudioEnd("{\"CMD\": \"PAUSE\"}");
    }

    private void sendAudioSubtitles() {
        sendAudioEnd("{\"CMD\": \"SUBTITLES\"}");
    }

    private void sendAudioVolUp() {
        sendAudioEnd("{\"CMD\": \"VOL_UP\"}");
    }

    private void sendAudioVolDown() {
        sendAudioEnd("{\"CMD\": \"VOL_DOWN\"}");
    }

    private void sendAudioPrevChapter() {
        sendAudioEnd("{\"CMD\": \"PREV_CHAPTER\"}");
    }

    private void sendAudioNextChapter() {
        sendAudioEnd("{\"CMD\": \"NEXT_CHAPTER\"}");
    }

    private void sendAudioEnd(final String request) {

        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://"+ MediaModel.getInstance().getIpAddress()+":21121/music/cmd", request);
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
