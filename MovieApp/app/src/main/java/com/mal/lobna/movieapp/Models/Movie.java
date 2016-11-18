package com.mal.lobna.movieapp.Models;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class Movie {
 //   int id;
    String original_title;
    String overview;
    String popularity;
    String poster_path;
    String release_date;
    String title;
    String vote_average;
    String vote_count;
    boolean favourite;

  //  public void setId(int id) { this.id = id; }

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

  //  public int getId(){ return id; }

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
