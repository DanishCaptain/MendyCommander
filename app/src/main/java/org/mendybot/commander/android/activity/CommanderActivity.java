package org.mendybot.commander.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.activity.music.ScheduleMusicActivity;
import org.mendybot.commander.android.model.MediaModel;

public class CommanderActivity extends AppCompatActivity implements View.OnClickListener {

    private View bSendVideoCommands;
    private View bSendAudioCommands;
    private View bScheduleMovies;
    private View bScheduleTvShows;
    private View bScheduleMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MediaModel.getInstance().init(this);

        setContentView(R.layout.activity_commander);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bSendVideoCommands = findViewById(R.id.send_video_commands);
        bSendVideoCommands.setOnClickListener(this);
        bSendAudioCommands = findViewById(R.id.send_audio_commands);
        bSendAudioCommands.setOnClickListener(this);
        bScheduleMovies = findViewById(R.id.schedule_movies);
        bScheduleMovies.setOnClickListener(this);
        bScheduleTvShows = findViewById(R.id.schedule_tv_shows);
        bScheduleTvShows.setOnClickListener(this);
        bScheduleMusic = findViewById(R.id.schedule_music);
        bScheduleMusic.setOnClickListener(this);
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public void onClick(View view) {
        if (view == bSendVideoCommands) {
            Intent intent = new Intent(this, VideoCommandsActivity.class);
            startActivity(intent);
        } else if (view == bSendAudioCommands) {
            Intent intent = new Intent(this, AudioCommandsActivity.class);
            startActivity(intent);
        } else if (view == bScheduleMovies) {
            Intent intent = new Intent(this, ScheduleMoviesActivity.class);
            startActivity(intent);
        } else if (view == bScheduleTvShows) {
            Intent intent = new Intent(this, ScheduleTvShowsActivity.class);
            startActivity(intent);
        } else if (view == bScheduleMusic) {
            Intent intent = new Intent(this, ScheduleMusicActivity.class);
            startActivity(intent);
        }
    }

}
