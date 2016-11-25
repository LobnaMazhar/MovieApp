package com.mal.lobna.movieapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mal.lobna.movieapp.Models.Trailer;
import com.mal.lobna.movieapp.Listeners.OnTrailerClickListener;
import com.mal.lobna.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Lobna on 18-Nov-16.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Trailer> trailers;
    private OnTrailerClickListener listener;

    public TrailerAdapter(Activity activity, ArrayList<Trailer> trailers, OnTrailerClickListener listener){
        this.activity = activity;
        this.trailers = trailers;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = activity.getLayoutInflater().inflate(R.layout.movie_traileritem, parent, false);

        return new TrailerAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerNumber.setText(trailers.get(position).getName());

        holder.bind(trailers.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView trailerNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerNumber = (TextView) itemView.findViewById(R.id.trailerNumber);
        }

        public void bind(final Trailer item, final OnTrailerClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTrailerClick(item);
                }
            });
        }
    }
}
