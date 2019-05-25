package org.mendybot.commander.android.activity.tv;

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
import org.mendybot.commander.android.domain.TvEpisode;
import org.mendybot.commander.android.domain.TvSeason;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.model.list.TvSeasonListListener;

import java.util.Collections;
import java.util.List;

public class ScheduleSeriesActivity extends AppCompatActivity {
    private static final String TAG = ScheduleSeriesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_tv_series);

        RecyclerView recyclerView = findViewById(R.id.series_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().addTvSeasonListListener(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        RecyclerView recyclerView = findViewById(R.id.series_list);
        SimpleItemRecyclerViewAdapter adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
        MediaModel.getInstance().removeTvSeasonListListener(adapter);
        super.onDestroy();
        System.gc();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.SeriesViewHolder>
            implements TvSeasonListListener, View.OnClickListener {

        private final ScheduleSeriesActivity mParentActivity;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvSeason item = (TvSeason) view.getTag();
                select(item);
            }
        };

        private void select(TvSeason item) {
            MediaModel.getInstance().setActiveSeason(item);

            if (mParentActivity.getSupportActionBar() != null && item != null) {
                mParentActivity.getSupportActionBar().setTitle(mParentActivity.getResources().getString(R.string.title_activity_schedule_tv_shows) + " - " + item.getTitle());
                TextView mSelectedSeason = mParentActivity.findViewById(R.id.selected_season_name);
                mSelectedSeason.setText(item.getTitle());
                TextView mSelectedSeries = mParentActivity.findViewById(R.id.selected_series_name);
                mSelectedSeries.setText(item.getSeries().getTitle());
            }

            Button mSelectedSeriesButton = mParentActivity.findViewById(R.id.selected_series_button);
            if (mSelectedSeriesButton.getVisibility() == Button.INVISIBLE) {
                mSelectedSeriesButton.setVisibility(Button.VISIBLE);
            }
            mSelectedSeriesButton.setOnClickListener(this);
            Button mSubmitAllEpisodesButton = mParentActivity.findViewById(R.id.submit_all_episodes_button);
            if (mSubmitAllEpisodesButton.getVisibility() == Button.INVISIBLE) {
                mSubmitAllEpisodesButton.setVisibility(Button.VISIBLE);
            }
            mSubmitAllEpisodesButton.setOnClickListener(this);

        }

        SimpleItemRecyclerViewAdapter(ScheduleSeriesActivity parent) {
            mParentActivity = parent;
        }

        @Override
        @NonNull
        public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_series_item_content, parent, false);
            return new SeriesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SeriesViewHolder holder, int position) {
            TvSeason current = MediaModel.getInstance().getTvSeasons().get(position);
            String sTitle = "["+current.getSeries().getTitle()+"]";
            holder.mSeasonName.setText(sTitle);
            holder.mSeriesName.setText(current.getTitle());
            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return MediaModel.getInstance().getTvSeasons().size();
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
            if (view.getId() == R.id.selected_series_button) {
                Intent intent = new Intent(mParentActivity, ScheduleShowActivity.class);
                mParentActivity.startActivity(intent);
            } else if (view.getId() == R.id.submit_all_episodes_button) {
                TvSeason a = MediaModel.getInstance().getActiveSeason();
                List<TvEpisode> list = a.getEpisodes();
                Collections.sort(list);
                for (TvEpisode track : list) {
                    List<MediaFile> files = track.getFiles();
                    ScheduleShowActivity.schedule(track.getTitle(), files);
                }
            }
        }

        class SeriesViewHolder extends RecyclerView.ViewHolder {
            final TextView mSeriesName;
            final TextView mSeasonName;

            SeriesViewHolder(View view) {
                super(view);
                mSeriesName = view.findViewById(R.id.series_name);
                mSeasonName = view.findViewById(R.id.season_name);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
