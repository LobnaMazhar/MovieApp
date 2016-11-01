package com.mal.lobna.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.mal.lobna.movieapp.MovieContract.MovieTable;

/**
 * Created by Lobna on 21-Oct-16.
 */

public class MovieDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABSE_NAME = "movies.db";

    public MovieDBHandler(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createFavouritesTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldDBVersion, int newDBVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieTable.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public void createFavouritesTable(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + MovieTable.TABLE_NAME + " ( " +
                MovieTable.COLOUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_POSTER + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_OVERVIEW + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING + " TEXT DEFAULT 0, " +
                MovieTable.COLOUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieTable.COLOUMN_MOVIE_FAVOURITE + " INTEGER NOT NULL DEFAULT 0 " +
                ");";

        sqLiteDatabase.execSQL(query);
    }

    public void favouriteMovie(String originalTitle, String poster, String overview, String averageVoting, String releaseDate){
        ContentValues values = new ContentValues();

        values.put(MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE, originalTitle);
        values.put(MovieTable.COLOUMN_MOVIE_POSTER, poster);
        values.put(MovieTable.COLOUMN_MOVIE_OVERVIEW, overview);
        values.put(MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING, averageVoting);
        values.put(MovieTable.COLOUMN_MOVIE_RELEASE_DATE, releaseDate);
        values.put(MovieTable.COLOUMN_MOVIE_FAVOURITE, 1);

        getWritableDatabase().insert(MovieTable.TABLE_NAME, null, values);
    }

    public ArrayList<Movie> getFavourites(){
        ArrayList<Movie> favouriteMovies = new ArrayList<>();

        String query = "SELECT * FROM  " + MovieTable.TABLE_NAME + " WHERE " + MovieTable.COLOUMN_MOVIE_FAVOURITE + " = 1";

        Cursor c = getWritableDatabase().rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Movie movie = new Movie();

            movie.original_title = c.getString(c.getColumnIndex(MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE));
            movie.poster_path = c.getString(c.getColumnIndex(MovieTable.COLOUMN_MOVIE_POSTER));
            movie.overview = c.getString(c.getColumnIndex(MovieTable.COLOUMN_MOVIE_OVERVIEW));
            movie.vote_average = c.getString(c.getColumnIndex(MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING));
            movie.release_date = c.getString(c.getColumnIndex(MovieTable.COLOUMN_MOVIE_RELEASE_DATE));

            favouriteMovies.add(movie);

            c.moveToNext();
        }
        c.close();
        return favouriteMovies;
    }
}
