package com.mal.lobna.movieapp;

import android.provider.BaseColumns;

/**
 * Created by Lobna on 22-Oct-16.
 */
public class MovieContract {

    public static final class MovieTable implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLOUMN_ID = "_id";
        public static final String COLOUMN_MOVIE_ORIGINAL_TITLE = "movieoriginaltitle";
        public static final String COLOUMN_MOVIE_POSTER = "movieposter";
        public static final String COLOUMN_MOVIE_OVERVIEW = "movieoverview";
        public static final String COLOUMN_MOVIE_AVERAGE_VOTING = "movieaveragevoting";
        public static final String COLOUMN_MOVIE_RELEASE_DATE = "moviereleasedate";
        public static final String COLOUMN_MOVIE_FAVOURITE = "moviefavourite";
    }
}
