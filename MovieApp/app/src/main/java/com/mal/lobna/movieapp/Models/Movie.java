package com.mal.lobna.movieapp.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class Movie implements Serializable {
    private int id;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private String vote_average;
    private boolean favourite;

    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    public void setId(int id) { this.id = id; }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public int getId(){ return id; }

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
