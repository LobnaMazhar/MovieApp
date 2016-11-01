package com.mal.lobna.movieapp;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class Movie {
    String id;
    String original_title;
    String overview;
    String popularity;
    String poster_path;
    String release_date;
    String title;
    String vote_average;
    String vote_count;

    public String getId(){
        return id;
    }

    public String getOriginal_title(){
        return original_title;
    }

    public String getPoster_path(){
        return poster_path;
    }

    public String getOverview(){
        return overview;
    }

    public String getVote_average(){
        return vote_average;
    }

    public String getRelease_date(){
        return release_date;
    }
}
