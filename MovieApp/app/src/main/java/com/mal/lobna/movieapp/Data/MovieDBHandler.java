package com.mal.lobna.movieapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mal.lobna.movieapp.Data.MovieContract.MovieTable;


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
        createMovieTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldDBVersion, int newDBVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieTable.MOVIE_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void createMovieTable(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + MovieTable.MOVIE_TABLE + " ( " +
                MovieTable.COLOUMN_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_POSTER + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_OVERVIEW + " TEXT NOT NULL UNIQUE, " +
                MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING + " TEXT DEFAULT 0, " +
                MovieTable.COLOUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieTable.COLOUMN_MOVIE_FAVOURITE + " INTEGER NOT NULL DEFAULT 0 " +
                ");";

        sqLiteDatabase.execSQL(query);
    }
}
