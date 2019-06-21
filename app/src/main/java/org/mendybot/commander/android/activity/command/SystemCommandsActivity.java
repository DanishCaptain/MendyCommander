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

public class SystemCommandsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SystemCommandsActivity.class.getSimpleName();
    private View bSendAppRestart;
    private View bSendAppShutdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_commands);

        bSendAppRestart = findViewById(R.id.send_app_restart_command);
        bSendAppRestart.setOnClickListener(this);
        bSendAppShutdown = findViewById(R.id.send_app_shutdown_command);
        bSendAppShutdown.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == bSendAppRestart) {
            sendAppRestart();
        } else if (view == bSendAppShutdown) {
            sendAppShutdown();
        }
    }

    private void sendAppRestart() {
        sendCmd("{\"uuid\": \"QUIT\", \"cmd\": \"RESTART\"}");
    }

    private void sendAppShutdown() {
        sendCmd("{\"uuid\": \"QUIT\", \"cmd\": \"SHUTDOWN\"}");
    }

    private void sendCmd(final String request) {

        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://"+ MediaModel.getInstance().getHost()+":2412/app-cmd", request);
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
