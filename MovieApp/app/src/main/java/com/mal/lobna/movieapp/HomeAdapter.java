package com.mal.lobna.movieapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeAdapter extends ArrayAdapter<Movie> {

    Context context;
    ArrayList<Movie> movies;

    public HomeAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.movie_griditem, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //LayoutInflater inflater = LayoutInflater.from(getContext());

        View movieGridView = inflater.inflate(R.layout.movie_griditem, parent, false);

        Movie movieObject = movies.get(position);


        ImageView moviePosterImageViewGridItem = (ImageView) movieGridView.findViewById(R.id.moviePosterImageViewGridItem);
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "w342/";
        String posterPath = movieObject.getPoster_path();
        Picasso.with(context).load(baseURL + size + posterPath).into(moviePosterImageViewGridItem);

    //    TextView movieNameTextViewGridItem = (TextView) movieGridView.findViewById(R.id.movieNameTextViewGridItem);
    //    movieNameTextViewGridItem.setText(movieObject.getTitle());

        return movieGridView;
    }

}
