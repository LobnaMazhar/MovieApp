/*
package com.mal.lobna.movieapp.syncService;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mal.lobna.movieapp.Models.Movie;

*/
/**
 * Created by Lobna on 18-Nov-16.
 *//*


public class MovieSyncService extends Service {

    private static final Object movieSyncAdapterLock = new Object();
    private static MovieSyncAdapter movieSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (movieSyncAdapterLock){
            if(movieSyncAdapter == null){
                movieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return movieSyncAdapter.getSyncAdapterBinder();
    }
}
*/
