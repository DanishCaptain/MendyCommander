package org.mendybot.commander.android.activity.tv;

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
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.model.list.TvEpisodeListListener;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.List;

public class ScheduleShowActivity extends AppCompatActivity {
    private static final String TAG = ScheduleShowActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_tv_show);

        TvSeason item = MediaModel.getInstance().getActiveSeason();
        if (getSupportActionBar() != null && item != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_schedule_tv_shows) + " - " + item.getTitle());
        }

        RecyclerView recyclerView = findViewById(R.id.show_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().addTvEpisodeListListener(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        RecyclerView recyclerView = findViewById(R.id.show_list);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().removeTvEpisodeListListener(adapter);
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
                ff.setTitle(name+" part "+(counter++));
                ff.setAnnounce(false);
            }
        }
        final String request = g.toJson(vv);
        Log.d(TAG, "request: "+request);
        new Thread() {
            @Override
            public void run() {
                String rr = UrlUtility.exchangeJson("http://"+MediaModel.getInstance().getHost()+":21122/video", request);
                Log.d(TAG, "response: "+rr);
            }
        }.start();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ShowViewHolder>
            implements TvEpisodeListListener, View.OnClickListener {

        private final ScheduleShowActivity mParentActivity;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvEpisode item = (TvEpisode) view.getTag();
                select(item);
            }
        };

        private void select(TvEpisode item) {
            MediaModel.getInstance().setActiveEpisode(item);
            if (mParentActivity.getSupportActionBar() != null && item != null) {
                mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_tv_shows) + " - " + item.getSeason().getTitle() + " - " + item.getTitle());
                TextView mSeasonTitle = mParentActivity.findViewById(R.id.selected_season_title);
                mSeasonTitle.setText(item.getSeason().getSeries().getTitle());
                TextView mSeriesTitle = mParentActivity.findViewById(R.id.selected_series_title);
                mSeriesTitle.setText(item.getSeason().getTitle());
                TextView mEpisodeTitle = mParentActivity.findViewById(R.id.selected_episode_title);
                mEpisodeTitle.setText(item.getTitle());
            }
            Button mSubmitEpisideButton = mParentActivity.findViewById(R.id.submit_episode_button);
            mSubmitEpisideButton.setVisibility(Button.VISIBLE);
            if (mSubmitEpisideButton.getVisibility() == Button.INVISIBLE) {
                mSubmitEpisideButton.setVisibility(Button.VISIBLE);
            }
            mSubmitEpisideButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleShowActivity parent) {
            mParentActivity = parent;
        }

        @Override
        @NonNull
        public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_show_item_content, parent, false);
            return new ShowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ShowViewHolder holder, int position) {
            TvEpisode current = MediaModel.getInstance().getActiveSeason().getEpisodes().get(position);
            holder.mEpisodeTitle.setText(current.getTitle());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return MediaModel.getInstance().getActiveSeason().getEpisodes().size();
        }

        @Override
        public void listChanged() {
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.submit_episode_button) {
                TvEpisode t = MediaModel.getInstance().getActiveEpisode();
                List<MediaFile> list = t.getFiles();
                ScheduleShowActivity.schedule(t.getTitle(), list);
            }
        }

        class ShowViewHolder extends RecyclerView.ViewHolder {
            final TextView mEpisodeTitle;

            ShowViewHolder(View view) {
                super(view);
                mEpisodeTitle = view.findViewById(R.id.episode_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
