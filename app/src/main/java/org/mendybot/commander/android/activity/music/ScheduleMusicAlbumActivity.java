package org.mendybot.commander.android.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import org.mendybot.commander.android.model.list.AlbumListListener;

import java.util.List;

public class ScheduleMusicAlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_music_album);

        RecyclerView recyclerView = findViewById(R.id.albumn_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().addAlbumListListener(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        RecyclerView recyclerView = findViewById(R.id.albumn_list);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().removeAlbumListListener(adapter);
        super.onDestroy();
        System.gc();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder>
            implements AlbumListListener, View.OnClickListener {

        private final ScheduleMusicAlbumActivity mParentActivity;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongAlbum item = (SongAlbum) view.getTag();
                select(item);
            }
        };

        private void select(SongAlbum item) {
            if (mParentActivity.getSupportActionBar() != null && item != null) {
                MediaModel.getInstance().setActiveAlbum(item);

                mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_music) + " - " + item.getTitle());

                TextView mMusicSelectedArtistView = mParentActivity.findViewById(R.id.music_selected_albumn_artist);
                mMusicSelectedArtistView.setText(item.getArtist().getName());
                TextView mMusicSelectedAlbumnView = mParentActivity.findViewById(R.id.music_selected_albumn_name);
                mMusicSelectedAlbumnView.setText(item.getTitle());
                Button mMusicSelectedAlbumnButton = mParentActivity.findViewById(R.id.music_selected_albumn_button);
                if (mMusicSelectedAlbumnButton.getVisibility() == Button.INVISIBLE) {
                    mMusicSelectedAlbumnButton.setVisibility(Button.VISIBLE);
                }
                mMusicSelectedAlbumnButton.setOnClickListener(this);
                Button mMusicSubmitAllAlbumnButton = mParentActivity.findViewById(R.id.music_submit_all_albumn_button);
                if (mMusicSubmitAllAlbumnButton.getVisibility() == Button.INVISIBLE) {
                    mMusicSubmitAllAlbumnButton.setVisibility(Button.VISIBLE);
                }
                mMusicSubmitAllAlbumnButton.setOnClickListener(this);
            }
        }

        SimpleItemRecyclerViewAdapter(ScheduleMusicAlbumActivity parent) {
            mParentActivity = parent;
        }

        @Override
        @NonNull
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_albumn_item_content, parent, false);
            return new MusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MusicViewHolder holder, int position) {
            SongAlbum current = MediaModel.getInstance().getAlbums().get(position);
            String line = "[" + current.getArtist().getName() + "]";
            holder.mMusicArtistView.setText(line);
            holder.mMusicAlbumnView.setText(current.getTitle());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return MediaModel.getInstance().getAlbums().size();
        }

        @Override
        public void listChanged() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.music_selected_albumn_button) {
                Intent intent = new Intent(mParentActivity, ScheduleMusicTrackActivity.class);
                mParentActivity.startActivity(intent);
            } else if (view.getId() == R.id.music_submit_all_albumn_button) {
                SongAlbum a = MediaModel.getInstance().getActiveAlbum();
                List<SongTrack> list = a.getTracks();
                for (SongTrack track : list) {
                    List<MediaFile> files = track.getFiles();
                    ScheduleMusicTrackActivity.schedule(track.getTitle(), files);
                }
            }
        }

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicArtistView;
            final TextView mMusicAlbumnView;

            MusicViewHolder(View view) {
                super(view);
                mMusicArtistView = view.findViewById(R.id.music_artist_name);
                mMusicAlbumnView = view.findViewById(R.id.music_albumn_name);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
