package org.mendybot.commander.android.activity.audioBook;

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
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.SongAlbum;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.model.MediaModel;

import java.util.Collections;
import java.util.List;

public class ScheduleAudioBookAlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_music_album);

        RecyclerView recyclerView = findViewById(R.id.albumn_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, MediaModel.getInstance().getAbAlbums()));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder> implements View.OnClickListener {

        private final ScheduleAudioBookAlbumActivity mParentActivity;
        private final List<SongAlbum> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongAlbum item = (SongAlbum) view.getTag();
                select(item);
            }
        };

        private void select(SongAlbum item) {
            MediaModel.getInstance().setActiveAbAlbum(item);

            mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_audio_book) + " - " + item.getTitle());

            TextView mMusicSelectedArtistView = (TextView) mParentActivity.findViewById(R.id.music_selected_albumn_artist);
            mMusicSelectedArtistView.setText(item.getArtist().getName());
            TextView mMusicSelectedAlbumnView = (TextView) mParentActivity.findViewById(R.id.music_selected_albumn_name);
            mMusicSelectedAlbumnView.setText(item.getTitle());
            Button mMusicSelectedAlbumnButton = (Button) mParentActivity.findViewById(R.id.music_selected_albumn_button);
            if (mMusicSelectedAlbumnButton.getVisibility() == Button.INVISIBLE) {
                mMusicSelectedAlbumnButton.setVisibility(Button.VISIBLE);
            }
            mMusicSelectedAlbumnButton.setOnClickListener(this);
            Button mMusicSubmitAllAlbumnButton = (Button) mParentActivity.findViewById(R.id.music_submit_all_albumn_button);
            if (mMusicSubmitAllAlbumnButton.getVisibility() == Button.INVISIBLE) {
                mMusicSubmitAllAlbumnButton.setVisibility(Button.VISIBLE);
            }
            mMusicSubmitAllAlbumnButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleAudioBookAlbumActivity parent,
                                      List<SongAlbum> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_albumn_item_content, parent, false);
            return new MusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MusicViewHolder holder, int position) {
            SongAlbum current = mValues.get(position);
            holder.mMusicArtistView.setText("[" + current.getArtist().getName() + "]");
            holder.mMusicAlbumnView.setText(current.getTitle());

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
                Intent intent = new Intent(mParentActivity, ScheduleAudioBookTrackActivity.class);
                mParentActivity.startActivity(intent);
            } else if (view.getId() == R.id.music_submit_all_albumn_button) {
                SongAlbum a = MediaModel.getInstance().getActiveAbAlbum();
                List<SongTrack> list = a.getTracks();
                for (SongTrack track : list) {
                    List<MediaFile> files = track.getFiles();
                    ScheduleAudioBookTrackActivity.schedule(track.getTitle(), files);
                }
            }
        }

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicArtistView;
            final TextView mMusicAlbumnView;

            MusicViewHolder(View view) {
                super(view);
                mMusicArtistView = (TextView) view.findViewById(R.id.music_artist_name);
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
