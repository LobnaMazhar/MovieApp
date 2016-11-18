package com.mal.lobna.movieapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.OnItemClickListener;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private final String LOG_TAG = HomeAdapter.class.getSimpleName();

    private  Activity activity;
    public ArrayList<Movie> movies;
    private OnItemClickListener listener;

    public HomeAdapter(Activity activity, ArrayList<Movie> movies, OnItemClickListener listener) {
        this.activity = activity;
        this.movies = movies;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.movie_griditem, parent, false);

        return new HomeAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "w342/";
        String posterPath = movies.get(position).getPoster_path();

        Log.v(LOG_TAG, baseURL + size + posterPath);
        Picasso.with(activity).load(baseURL + size + posterPath).into(holder.moviePosterImageView);

        holder.bind(movies.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePosterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.moviePosterImageViewGridItem);
            moviePosterImageView.setImageResource(R.drawable.movie_action);
        }

        public void bind(final Movie item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
