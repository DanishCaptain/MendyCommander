package org.mendybot.commander.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.activity.audioBook.ScheduleAudioBookAlbumActivity;
import org.mendybot.commander.android.activity.command.AudioCommandsActivity;
import org.mendybot.commander.android.activity.command.MediaStatusActivity;
import org.mendybot.commander.android.activity.command.SystemCommandsActivity;
import org.mendybot.commander.android.activity.command.VideoCommandsActivity;
import org.mendybot.commander.android.activity.movie.ScheduleMoviesActivity;
import org.mendybot.commander.android.activity.music.ScheduleMusicAlbumActivity;
import org.mendybot.commander.android.activity.tv.ScheduleSeriesActivity;
import org.mendybot.commander.android.model.MediaModel;

public class CommanderActivity extends AppCompatActivity implements View.OnClickListener {

    private View bSendSystemCommands;
    private View bSendVideoCommands;
    private View bSendAudioCommands;
    private View bScheduleMovies;
    private View bScheduleTvShows;
    private View bScheduleMusic;
    private View bScheduleAudioBook;
    private View bMediaStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MediaModel.getInstance().init(this);

        setContentView(R.layout.activity_commander);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        bSendSystemCommands = findViewById(R.id.send_system_commands);
        bSendSystemCommands.setOnClickListener(this);
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
        bScheduleAudioBook = findViewById(R.id.schedule_audio_book);
        bScheduleAudioBook.setOnClickListener(this);
        bMediaStatus = findViewById(R.id.media_status);
        bMediaStatus.setOnClickListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaModel.getInstance().reloadData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == bSendSystemCommands) {
            Intent intent = new Intent(this, SystemCommandsActivity.class);
            startActivity(intent);
        } else if (view == bSendVideoCommands) {
            Intent intent = new Intent(this, VideoCommandsActivity.class);
            startActivity(intent);
        } else if (view == bSendAudioCommands) {
            Intent intent = new Intent(this, AudioCommandsActivity.class);
            startActivity(intent);
        } else if (view == bScheduleMovies) {
            Intent intent = new Intent(this, ScheduleMoviesActivity.class);
            startActivity(intent);
        } else if (view == bScheduleTvShows) {
            Intent intent = new Intent(this, ScheduleSeriesActivity.class);
            startActivity(intent);
        } else if (view == bScheduleMusic) {
            Intent intent = new Intent(this, ScheduleMusicAlbumActivity.class);
            startActivity(intent);
        } else if (view == bScheduleAudioBook) {
            Intent intent = new Intent(this, ScheduleAudioBookAlbumActivity.class);
            startActivity(intent);
        } else if (view == bMediaStatus) {
            Intent intent = new Intent(this, MediaStatusActivity.class);
            startActivity(intent);
        }
    }

}
