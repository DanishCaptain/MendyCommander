package org.mendybot.commander.android.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.activity.ScheduleMoviesActivity;
import org.mendybot.commander.android.domain.Albumn;
import org.mendybot.commander.android.domain.AudioFile;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.model.MediaModel;

import java.util.Collections;
import java.util.List;

public class ScheduleMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedue_music);


        RecyclerView recyclerView = findViewById(R.id.albumn_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, MediaModel.getInstance().getAlbumns()));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder> implements View.OnClickListener {

        private final ScheduleMusicActivity mParentActivity;
        private final List<Albumn> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Albumn item = (Albumn) view.getTag();
                select(item);
            }
        };

        private void select(Albumn item) {
            MediaModel.getInstance().setActive(item);

            mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_music) + " - " + item.getName());

            TextView mMusicSelectedArtistView = (TextView) mParentActivity.findViewById(R.id.music_selected_albumn_artist);
            mMusicSelectedArtistView.setText(item.getArtist().getName());
            TextView mMusicSelectedAlbumnView = (TextView) mParentActivity.findViewById(R.id.music_selected_albumn_name);
            mMusicSelectedAlbumnView.setText(item.getName());
            Button mMusicSelectedAlbumnButton = (Button) mParentActivity.findViewById(R.id.music_selected_albumn_button);
            if (mMusicSelectedAlbumnButton.getVisibility() == Button.INVISIBLE) {
                mMusicSelectedAlbumnButton.setVisibility(Button.VISIBLE);
            }
            mMusicSelectedAlbumnButton.setOnClickListener(this);
            Button mMusicSelectedAllAlbumnButton = (Button) mParentActivity.findViewById(R.id.music_selected_all_albumn_button);
            if (mMusicSelectedAllAlbumnButton.getVisibility() == Button.INVISIBLE) {
                mMusicSelectedAllAlbumnButton.setVisibility(Button.VISIBLE);
            }
            mMusicSelectedAllAlbumnButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleMusicActivity parent,
                                      List<Albumn> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albumn_item_content, parent, false);
            return new MusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MusicViewHolder holder, int position) {
            Albumn current = mValues.get(position);
            holder.mMusicArtistView.setText("[" + current.getArtist().getName() + "]");
            holder.mMusicAlbumnView.setText(current.getName());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.music_selected_albumn_button) {
                Intent intent = new Intent(mParentActivity, ScheduleMusicAlbumnActivity.class);
                mParentActivity.startActivity(intent);
            } else if (view.getId() == R.id.music_selected_all_albumn_button) {
                Albumn a = MediaModel.getInstance().getActive();
                List<SongTrack> list = a.getTracks();
                for (SongTrack track : list) {
                    List<AudioFile> files = track.getFiles();
                    ScheduleMusicAlbumnActivity.schedule(track.getName(), files);
                }
            }
        }

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicArtistView;
            final TextView mMusicAlbumnView;

            MusicViewHolder(View view) {
                super(view);
                mMusicArtistView = (TextView) view.findViewById(R.id.music_albumn_artist);
                mMusicAlbumnView = (TextView) view.findViewById(R.id.music_albumn_name);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
