package org.mendybot.commander.android.activity.music;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.domain.Albumn;
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleMusicTrackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_music_track);

        Albumn item = MediaModel.getInstance().getActiveAlbumn();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_schedule_music)+" - "+item.getName());

        RecyclerView recyclerView = findViewById(R.id.music_track_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Albumn albumn = MediaModel.getInstance().getActiveAlbumn();
        List<SongTrack> list;
        if (albumn == null) {
            list = new ArrayList<>();
        } else {
            list = albumn.getTracks();
        }
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, list));
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
                ff.setTitle(name+" part "+(counter++));
                ff.setAnnounce(false);
            }
        }
        final String request = g.toJson(vv);
        System.out.println(request);
        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://192.168.100.50:21121/audio", request);
                System.out.println(rr);
            }
        }.start();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder> implements View.OnClickListener {

        private final ScheduleMusicTrackActivity mParentActivity;
        private final List<SongTrack> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongTrack item = (SongTrack) view.getTag();
                select(item);
            }
        };

        private void select(SongTrack item) {
            MediaModel.getInstance().setActiveTrack(item);

            mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_music) + " - " + item.getAlbumn().getName()+" - "+item.getName());

            TextView mMusicTrackArtist = (TextView) mParentActivity.findViewById(R.id.music_selected_track_artist_name);
            mMusicTrackArtist.setText(item.getAlbumn().getArtist().getName());
            TextView mMusicTrackAlbumn = (TextView) mParentActivity.findViewById(R.id.music_selected_track_albumn_name);
            mMusicTrackAlbumn.setText(item.getAlbumn().getName());
            TextView mMusicTrackName = (TextView) mParentActivity.findViewById(R.id.music_selected_track_name);
            mMusicTrackName.setText(item.getName());
            Button mMusicSubmitTrackButton = (Button) mParentActivity.findViewById(R.id.music_submit_track_button);
            mMusicSubmitTrackButton.setVisibility(Button.VISIBLE);
            if (mMusicSubmitTrackButton.getVisibility() == Button.INVISIBLE) {
                mMusicSubmitTrackButton.setVisibility(Button.VISIBLE);
            }
            mMusicSubmitTrackButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleMusicTrackActivity parent,
                                      List<SongTrack> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_track_item_content, parent, false);
            return new MusicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MusicViewHolder holder, int position) {
            SongTrack current = mValues.get(position);
            holder.mMusicTrackNameView.setText(current.getName());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.music_submit_track_button) {
                SongTrack t = MediaModel.getInstance().getActiveTrack();
                List<MediaFile> list = t.getFiles();
                ScheduleMusicTrackActivity.schedule(t.getName(), list);
            }
        }

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicTrackNameView;

            MusicViewHolder(View view) {
                super(view);
                mMusicTrackNameView = (TextView) view.findViewById(R.id.track_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
