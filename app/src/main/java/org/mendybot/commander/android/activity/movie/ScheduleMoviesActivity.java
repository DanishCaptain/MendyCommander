package org.mendybot.commander.android.activity.movie;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mendybot.commander.android.R;
import org.mendybot.commander.android.domain.MediaFile;
import org.mendybot.commander.android.domain.Movie;
import org.mendybot.commander.android.model.MediaModel;
import org.mendybot.commander.android.tools.UrlUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleMoviesActivity extends AppCompatActivity {
    private static final String TAG = ScheduleMoviesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_movies);

        RecyclerView recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(MediaModel.getInstance().getMovies()));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MovieViewHolder> {

        private static final String TAG = SimpleItemRecyclerViewAdapter.class.getSimpleName();
        private final List<Movie> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie item = (Movie) view.getTag();
                schedule(item);
            }
    };

        private void schedule(Movie item) {
            GsonBuilder b = new GsonBuilder();
            Gson g = b.create();
            ArrayList<MediaFile> vv = new ArrayList<>(item.getFiles());

            if (vv.size() == 1) {
                MediaFile ff = vv.get(0);
                ff.setTitle(item.getTitle());
                ff.setAnnounce(false);
            } else {
                int counter = 1;
                for (MediaFile ff : vv) {
                    ff.setTitle(item.getTitle()+" part "+(counter++));
                    ff.setAnnounce(false);
                }
            }
            final String request = g.toJson(vv);
            Log.d(TAG, "result: "+request);
            new Thread() {
                @Override
                public void run() {
                    String rr = UrlUtility.exchangeJson("http://"+MediaModel.getInstance().getIpAddress()+":21122/video", request);
                    Log.d(TAG, "result: "+rr);
                }
            }.start();
        }

        SimpleItemRecyclerViewAdapter(List<Movie> items) {
            Collections.sort(items);
            mValues = items;
        }

        @Override
        @NonNull
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_content, parent, false);
            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
            Movie current = mValues.get(position);
            holder.mMovieTitleView.setText(current.getTitle());

            holder.itemView.setTag(current);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class MovieViewHolder extends RecyclerView.ViewHolder {
            final TextView mMovieTitleView;

            MovieViewHolder(View view) {
                super(view);
                mMovieTitleView = view.findViewById(R.id.movie_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
