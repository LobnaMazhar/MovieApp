package com.mal.lobna.movieapp.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Models.Movie;

import java.util.ArrayList;

/**
 * Created by Lobna on 12-Nov-16.
 */

public class MovieDataSource {
    private static MovieDataSource instance;
    private MovieDBHandler movieDBHandler;
    private SQLiteDatabase database;

    public static MovieDataSource getInstance() {
        if (instance == null) {
            instance = new MovieDataSource();
            instance.movieDBHandler = new MovieDBHandler(MovieApplication.getMovieApp().getApplicationContext());
            instance.open();
        }
        return instance;
    }

    public void open() {
        database = movieDBHandler.getWritableDatabase();
    }

    public void close() {
        if (database.isOpen())
            database.close();
    }

    private ContentValues getContentValuesFromMovie(Movie movie) {
        ContentValues movieContentValues = new ContentValues();

        movieContentValues.put(MovieContract.MovieTable.COLOUMN_ID, movie.getId());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginal_title());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER, movie.getPoster_path());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_OVERVIEW, movie.getOverview());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING, movie.getVote_average());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        movieContentValues.put(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE, movie.isFavourite() ? 1 : 0);

        return movieContentValues;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        Movie movie = new Movie();

        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_ID)));
        movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE)));
        movie.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_OVERVIEW)));
        movie.setVote_average(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING)));
        movie.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_RELEASE_DATE)));
        movie.setFavourite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE)) == 1);

        return movie;
    }

    public void insertList(ArrayList<Movie> movies) {
        deleteNonFavouriteMovies();

        for (Movie movie : movies) {
            ContentValues contentValues = getContentValuesFromMovie(movie);

            long id = database.insert(MovieContract.MovieTable.MOVIE_TABLE, null, contentValues);
            Log.v("id", Long.toString(id));
        }
    }

    public void markAsFav(Movie movie) {
        ContentValues contentValues = getContentValuesFromMovie(movie);
        int nRowsAffected = database.update(MovieContract.MovieTable.MOVIE_TABLE, contentValues
                , MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE + " = ?", new String[]{movie.getOriginal_title()});
    }

    public boolean isFav(int movieId) {
        Cursor cursor = database.query(MovieContract.MovieTable.MOVIE_TABLE, null,
                MovieContract.MovieTable.COLOUMN_ID + " = ?",
                new String[]{String.valueOf(movieId)}, null, null, null);
        cursor.moveToFirst();

        Movie movie = getMovieFromCursor(cursor);
        return movie.isFavourite();
    }

    public ArrayList<Movie> getFavourites() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = database.query(MovieContract.MovieTable.MOVIE_TABLE,
                null, MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE + " = ?", new String[]{"1"}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = getMovieFromCursor(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }

        cursor.close();
        return movies;
    }

    public void deleteNonFavouriteMovies() {
        database.delete(MovieContract.MovieTable.MOVIE_TABLE, MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE + " = ?", new String[]{"0"});
    }
}
