package org.mendybot.commander.android.activity.music;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.SongAlbum;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.model.list.TrackListListener;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.List;

public class ScheduleMusicTrackActivity extends AppCompatActivity {
    private static final String TAG = ScheduleMusicTrackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_music_track);

        SongAlbum item = MediaModel.getInstance().getActiveAlbum();
        if (getSupportActionBar() != null && item != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_schedule_music) + " - " + item.getTitle());
        }

        RecyclerView recyclerView = findViewById(R.id.music_track_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().addTrackListListener(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        RecyclerView recyclerView = findViewById(R.id.music_track_list);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().removeTrackListListener(adapter);
        super.onDestroy();
        System.gc();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this));
    }

    public static void schedule(String name, List<MediaFile> vv) {
        GsonBuilder b = new GsonBuilder();
        Gson g = b.create();

        if (vv.size() == 1) {
            MediaFile ff = vv.get(0);
            ff.setTitle(name);
            ff.setAnnounce(false);
        } else {
            int counter = 1;
            for (MediaFile ff : vv) {
                ff.setTitle(name + " part " + (counter++));
                ff.setAnnounce(false);
            }
        }
        final String request = g.toJson(vv);
        Log.d(TAG, "request: " + request);
        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://"+MediaModel.getInstance().getHost()+":21121/audio", request);
                Log.d(TAG, "result: " + rr);
            }
        }.start();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder>
            implements TrackListListener, View.OnClickListener {

        private final ScheduleMusicTrackActivity mParentActivity;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongTrack item = (SongTrack) view.getTag();
                select(item);
            }
        };

        private void select(SongTrack item) {
            MediaModel.getInstance().setActiveTrack(item);
            if (mParentActivity.getSupportActionBar() != null && item != null) {
                mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_music) + " - " + item.getAlbumn().getTitle() + " - " + item.getTitle());
                TextView mMusicTrackArtist = mParentActivity.findViewById(R.id.music_selected_track_artist_name);
                mMusicTrackArtist.setText(item.getAlbumn().getArtist().getName());
                TextView mMusicTrackAlbumn = mParentActivity.findViewById(R.id.music_selected_track_albumn_name);
                mMusicTrackAlbumn.setText(item.getAlbumn().getTitle());
                TextView mMusicTrackName = mParentActivity.findViewById(R.id.music_selected_track_name);
                mMusicTrackName.setText(item.getTitle());
            }
            Button mMusicSubmitTrackButton = mParentActivity.findViewById(R.id.music_submit_track_button);
            mMusicSubmitTrackButton.setVisibility(Button.VISIBLE);
            if (mMusicSubmitTrackButton.getVisibility() == Button.INVISIBLE) {
                mMusicSubmitTrackButton.setVisibility(Button.VISIBLE);
            }
            mMusicSubmitTrackButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleMusicTrackActivity parent) {
            mParentActivity = parent;
        }

        @Override
        @NonNull
        public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_track_item_content, parent, false);
            return new MusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MusicViewHolder holder, int position) {
            SongTrack current = MediaModel.getInstance().getActiveAlbum().getTracks().get(position);
            holder.mMusicTrackNameView.setText(current.getTitle());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return MediaModel.getInstance().getActiveAlbum().getTracks().size();
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
            if (view.getId() == R.id.music_submit_track_button) {
                SongTrack t = MediaModel.getInstance().getActiveTrack();
                List<MediaFile> list = t.getFiles();
                ScheduleMusicTrackActivity.schedule(t.getTitle(), list);
            }
        }

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicTrackNameView;

            MusicViewHolder(View view) {
                super(view);
                mMusicTrackNameView = view.findViewById(R.id.track_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
