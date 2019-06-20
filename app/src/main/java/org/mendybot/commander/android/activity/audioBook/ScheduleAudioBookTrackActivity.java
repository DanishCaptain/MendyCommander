package org.mendybot.commander.android.activity.audioBook;

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

public class ScheduleAudioBookTrackActivity extends AppCompatActivity {
    private static final String TAG = ScheduleAudioBookTrackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_music_track);

        SongAlbum item = MediaModel.getInstance().getActiveAbAlbum();
        if (getSupportActionBar() != null && item != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_schedule_audio_book) + " - " + item.getTitle());
        }

        RecyclerView recyclerView = findViewById(R.id.music_track_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().addAbTrackListListener(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        RecyclerView recyclerView = findViewById(R.id.music_track_list);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().removeAbTrackListListener(adapter);
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
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.AudioBookViewHolder>
            implements TrackListListener, View.OnClickListener {

        private final ScheduleAudioBookTrackActivity mParentActivity;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongTrack item = (SongTrack) view.getTag();
                select(item);
            }
        };

        private void select(SongTrack item) {
            MediaModel.getInstance().setActiveAbTrack(item);
            if (mParentActivity.getSupportActionBar() != null && item != null) {
                mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_audio_book) + " - " + item.getAlbumn().getTitle() + " - " + item.getTitle());
                TextView mAudioBookTrackArtist = mParentActivity.findViewById(R.id.music_selected_track_artist_name);
                mAudioBookTrackArtist.setText(item.getAlbumn().getArtist().getName());
                TextView mAudioBookTrackAlbumn = mParentActivity.findViewById(R.id.music_selected_track_albumn_name);
                mAudioBookTrackAlbumn.setText(item.getAlbumn().getTitle());
                TextView mAudioBookTrackName = mParentActivity.findViewById(R.id.music_selected_track_name);
                mAudioBookTrackName.setText(item.getTitle());
            }
            Button mAudioBookSubmitTrackButton = mParentActivity.findViewById(R.id.music_submit_track_button);
            mAudioBookSubmitTrackButton.setVisibility(Button.VISIBLE);
            if (mAudioBookSubmitTrackButton.getVisibility() == Button.INVISIBLE) {
                mAudioBookSubmitTrackButton.setVisibility(Button.VISIBLE);
            }
            mAudioBookSubmitTrackButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleAudioBookTrackActivity parent) {
            mParentActivity = parent;
        }

        @Override
        @NonNull
        public AudioBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_track_item_content, parent, false);
            return new AudioBookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AudioBookViewHolder holder, int position) {
            SongTrack current = MediaModel.getInstance().getActiveAbAlbum().getTracks().get(position);
            holder.mAudioBookTrackNameView.setText(current.getTitle());
            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return MediaModel.getInstance().getActiveAbAlbum().getTracks().size();
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
                SongTrack t = MediaModel.getInstance().getActiveAbTrack();
                List<MediaFile> list = t.getFiles();
                ScheduleAudioBookTrackActivity.schedule(t.getTitle(), list);
            }
        }

        class AudioBookViewHolder extends RecyclerView.ViewHolder {
            final TextView mAudioBookTrackNameView;

            AudioBookViewHolder(View view) {
                super(view);
                mAudioBookTrackNameView = view.findViewById(R.id.track_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
