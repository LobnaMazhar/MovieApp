package com.mal.lobna.movieapp.Listeners;

import com.mal.lobna.movieapp.Models.Trailer;

import java.util.ArrayList;


/**
 * Created by Lobna on 18-Nov-16.
 */

public interface TrailerListener {
    public void onDownloadFinished(ArrayList<Trailer> trailers);

    public void onFail(Exception e);
}
