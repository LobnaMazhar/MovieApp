package com.mal.lobna.movieapp.Application;

import android.app.Application;

/**
 * Created by Lobna on 12-Nov-16.
 */

public class MovieApplication extends Application {
    public static MovieApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MovieApplication getMovieApp() {
        return instance;
    }
}
