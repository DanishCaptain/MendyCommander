package org.mendybot.commander.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.model.MediaModel;

public class CommanderActivity extends AppCompatActivity implements View.OnClickListener {

    private View bSendCommands;
    private View bScheduleMovies;
    private View bScheduleTvShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MediaModel.getInstance().init(this);

        setContentView(R.layout.activity_commander);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bSendCommands = findViewById(R.id.send_commands);
        bSendCommands.setOnClickListener(this);
        bScheduleMovies = findViewById(R.id.schedule_movies);
        bScheduleMovies.setOnClickListener(this);
        bScheduleTvShows = findViewById(R.id.schedule_tv_shows);
        bScheduleTvShows.setOnClickListener(this);
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
        if (view == bSendCommands) {
            Intent intent = new Intent(this, VideoCommandsActivity.class);
            startActivity(intent);
        } else if (view == bScheduleMovies) {
            Intent intent = new Intent(this, ScheduleMoviesActivity.class);
            startActivity(intent);
        } else if (view == bScheduleTvShows) {
            Intent intent = new Intent(this, ScheduleTvShowsActivity.class);
            startActivity(intent);
        }
    }

}
