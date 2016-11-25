package com.mal.lobna.movieapp.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Models.Movie;

/**
 * Created by Lobna on 12-Nov-16.
 */

public class Utilities {

    public static boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) MovieApplication.getMovieApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static void noInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MovieApplication.getMovieApp().getApplicationContext());
        builder.setTitle("Internet connection");
        builder.setMessage("Connect to a network");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
