/*
package com.mal.lobna.movieapp.syncService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.BuildConfig;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

*/
/**
 * Created by Lobna on 18-Nov-16.
 *//*


public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    private String LOG_TAG = MovieSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            String baseURL = "https://api.themoviedb.org/3/discover/movie?";

            Context context = MovieApplication.getMovieApp().getApplicationContext();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String sortBy = sharedPreferences.getString(context.getString(R.string.sortMoviesBy_prefKey), context.getResources().getString(R.string.sortMoviesBy_defaultPrefValue));

            if (sortBy.equals(R.string.favourites_prefValueID)) {
                Uri builtUri = Uri.parse(baseURL).buildUpon().appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY).appendQueryParameter("sort_by", sortBy).build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movie JSON string : " + movieJsonStr);

                getMovieDataFromJson(movieJsonStr);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            movieJsonStr = null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
       */
/* try {
            if (movieJsonStr == null) {
                return new ArrayList<>(); // TODO a3'yrha l calling fn getFavourites();
            } else
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }*//*

        return;
    }

    private void getMovieDataFromJson(String movieJsonStr) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject reader = new JSONObject(movieJsonStr);
        JSONArray results = reader.getJSONArray("results");

        for (int i = 0; i < results.length(); ++i) {
            JSONObject movieData = results.getJSONObject(i);

            Movie movieObject = new Movie();

            //   movieObject.setId(Integer.valueOf(movieData.getString("id")));
            movieObject.setOriginal_title(movieData.getString("original_title"));
            movieObject.setPoster_path(movieData.getString("poster_path"));
            movieObject.setOverview(movieData.getString("overview"));
            movieObject.setVote_average(movieData.getString("vote_average"));
            movieObject.setRelease_date(movieData.getString("release_date"));
            movieObject.setFavourite(false);
            // movieObject.popularity = movieData.getString("popularity");
            // movieObject.title = movieData.getString("title");
            // movieObject.vote_count = movieData.getString("vote_count");

            // Log.v(LOG_TAG, "current iteration   i = " + Integer.toString(i) +  "    current ID : " + movieData.getString("id") + "   current poster+path : " + reader.getString("poster_path"));

            movies.add(movieObject);
        }
        saveMovies(movies);
    }

    public void saveMovies(ArrayList<Movie> movies) {
        MovieDataSource.getInstance().insertList(movies);
    }

    public static void syncImmediately() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Context context = MovieApplication.getMovieApp().getApplicationContext();
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    */
/**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     *//*

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        */
/*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         *//*

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        */
/*
         * Since we've created an account
         *//*

        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        */
/*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         *//*

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        */
/*
         * Finally, let's do a sync to get things started
         *//*

        syncImmediately();
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
*/
