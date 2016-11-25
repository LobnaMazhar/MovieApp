package com.mal.lobna.movieapp.Listeners;

import com.mal.lobna.movieapp.Models.Movie;

import java.util.ArrayList;

/**
 * Created by Lobna on 18-Nov-16.
 */

public interface MoviesListener {
    public void onDownloadFinished(ArrayList<Movie> movies);

    public void onFail(Exception e);
}
