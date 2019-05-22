package org.mendybot.commander.android.activity.tv;

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
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_tv_show);

        TvSeason item = MediaModel.getInstance().getActiveSeason();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_schedule_tv_shows)+" - "+item.getName());

        RecyclerView recyclerView = findViewById(R.id.show_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        TvSeason albumn = MediaModel.getInstance().getActiveSeason();
        List<TvEpisode> list;
        if (albumn == null) {
            list = new ArrayList<>();
        } else {
            list = albumn.getEpisodes();
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
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ShowViewHolder> implements View.OnClickListener {

        private final ScheduleShowActivity mParentActivity;
        private final List<TvEpisode> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvEpisode item = (TvEpisode) view.getTag();
                select(item);
            }
        };

        private void select(TvEpisode item) {
            MediaModel.getInstance().setActiveEpisode(item);

            mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_tv_shows) + " - " + item.getSeason().getName()+" - "+item.getName());

            TextView mSeasonTitle = mParentActivity.findViewById(R.id.selected_season_title);
            mSeasonTitle.setText(item.getSeason().getSeries().getName());
            TextView mSeriesTitle = mParentActivity.findViewById(R.id.selected_series_title);
            mSeriesTitle.setText(item.getSeason().getName());
            TextView mEpisodeTitle = (TextView) mParentActivity.findViewById(R.id.selected_episode_title);
            mEpisodeTitle.setText(item.getName());
            Button mSubmitEpisideButton = (Button) mParentActivity.findViewById(R.id.submit_episode_button);
            mSubmitEpisideButton.setVisibility(Button.VISIBLE);
            if (mSubmitEpisideButton.getVisibility() == Button.INVISIBLE) {
                mSubmitEpisideButton.setVisibility(Button.VISIBLE);
            }
            mSubmitEpisideButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleShowActivity parent,
                                      List<TvEpisode> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_show_item_content, parent, false);
            return new ShowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ShowViewHolder holder, int position) {
            TvEpisode current = mValues.get(position);
            holder.mEpisodeTitle.setText(current.getName());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.submit_episode_button) {
                TvEpisode t = MediaModel.getInstance().getActiveEpisode();
                List<MediaFile> list = t.getFiles();
                ScheduleShowActivity.schedule(t.getName(), list);
            }
        }

        class ShowViewHolder extends RecyclerView.ViewHolder {
            final TextView mEpisodeTitle;

            ShowViewHolder(View view) {
                super(view);
                mEpisodeTitle = (TextView) view.findViewById(R.id.episode_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
