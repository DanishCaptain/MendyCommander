package org.mendybot.commander.android.activity.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.domain.Albumn;
import org.mendybot.commander.android.domain.AudioFile;
import org.mendybot.commander.android.domain.SongTrack;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleMusicAlbumnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedue_music_albumn);


        View recyclerView = findViewById(R.id.music_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Albumn albumn = MediaModel.getInstance().getActive();
        List<SongTrack> list;
        if (albumn == null) {
            list = new ArrayList<>();
        } else {
            list = albumn.getTracks();
        }
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, list));
    }

    public static void schedule(String name, List<AudioFile> vv) {
        GsonBuilder b = new GsonBuilder();
        Gson g = b.create();

        if (vv.size() == 1) {
            AudioFile ff = vv.get(0);
            ff.setTitle(name);
            ff.setAnnounce(false);
        } else {
            int counter = 1;
            for (AudioFile ff : vv) {
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
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MusicViewHolder> {

        private final ScheduleMusicAlbumnActivity mParentActivity;
        private final List<SongTrack> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongTrack item = (SongTrack) view.getTag();
                schedule(item);
            }
        };

        private void schedule(SongTrack item) {
            ArrayList<AudioFile> vv = new ArrayList<>();
            vv.addAll(item.getFiles());
            mParentActivity.schedule(item.getName(), vv);
        }

        SimpleItemRecyclerViewAdapter(ScheduleMusicAlbumnActivity parent,
                                      List<SongTrack> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_content, parent, false);
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

        class MusicViewHolder extends RecyclerView.ViewHolder {
            final TextView mMusicTrackNameView;

            MusicViewHolder(View view) {
                super(view);
                mMusicTrackNameView = (TextView) view.findViewById(R.id.music_title);
            }
        }
    }
}
