package org.mendybot.commander.android.activity.movie;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_movies);


        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, MediaModel.getInstance().getMovies()));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.MovieViewHolder> {

        private final ScheduleMoviesActivity mParentActivity;
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
            ArrayList<MediaFile> vv = new ArrayList<>();
            vv.addAll(item.getFiles());

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
            System.out.println(request);
            new Thread() {
                @Override
                public void run() {
                    String rr = UrlUtility.exchangeJson("http://192.168.100.50:21122/video", request);
                    System.out.println(rr);
                }
            }.start();
        }

        SimpleItemRecyclerViewAdapter(ScheduleMoviesActivity parent,
                                      List<Movie> items) {
            Collections.sort(items);
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_content, parent, false);
            return new MovieViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieViewHolder holder, int position) {
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
                mMovieTitleView = (TextView) view.findViewById(R.id.movie_title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
