package org.mendybot.commander.android.activity.command;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.ArrayList;
import java.util.List;

public class MediaStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MediaStatusActivity.class.getSimpleName();
    private View bSendVideoStatus;
    private View bSendAudioStatus;
    private Spinner serverHostWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_status);

        bSendVideoStatus = findViewById(R.id.request_video_status);
        bSendVideoStatus.setOnClickListener(this);
        bSendAudioStatus = findViewById(R.id.request_audio_status);
        bSendAudioStatus.setOnClickListener(this);

        LinearLayout ll = findViewById(R.id.status_panel);
//        ll.setMovementMethod(new ScrollingMovementMethod());
        TextView tv = findViewById(R.id.status);
        tv.setMovementMethod(new ScrollingMovementMethod());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        System.out.println("fuck face 1 "+MediaModel.getInstance().getHost());
        serverHostWidget = findViewById(R.id.server_host);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                MediaModel.getInstance().getHosts()
        );
        int spinnerPosition = adapter.getPosition(MediaModel.getInstance().getHost());
        serverHostWidget.setAdapter(adapter);
        serverHostWidget.setSelection(spinnerPosition);
        MediaModel.getInstance().register(serverHostWidget);
//        System.out.println("fuck face 2 "+MediaModel.getInstance().getHost());
    }

    @Override
    public void onClick(View view) {
        String html;
        if (view == bSendVideoStatus) {
            html = sendVideoStatusRequest();
        } else if (view == bSendAudioStatus) {
            html = sendAudioStatusRequest();
        } else {
            html = "";
        }
        TextView tv = findViewById(R.id.status);
//        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setText(Html.fromHtml("<html><body>"+html+"</body></html>"));
//        tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
//        tv.setText(html);
    }

    private String sendVideoStatusRequest() {
        final StringBuilder rr = new StringBuilder();

        Thread t = new Thread() {
            @Override
            public void run() {
                rr.append(UrlUtility.grabHtml("http://"+ MediaModel.getInstance().getHost()+":21122/videos/status"));
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
        return rr.toString();
    }

    private String sendAudioStatusRequest() {
        final StringBuilder rr = new StringBuilder();

        Thread t = new Thread() {
            @Override
            public void run() {
                rr.append(UrlUtility.grabHtml("http://"+MediaModel.getInstance().getHost()+":21121/audios/status"));
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
        return rr.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
