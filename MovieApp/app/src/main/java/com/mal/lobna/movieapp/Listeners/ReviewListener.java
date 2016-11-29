package com.mal.lobna.movieapp.Listeners;

import com.mal.lobna.movieapp.Models.Review;

import java.util.ArrayList;

/**
 * Created by Lobna on 25-Nov-16.
 */

public interface ReviewListener {
    public void onDownloadFinished(ArrayList<Review> reviews);

    public void onFail(Exception e);
}
