package com.mal.lobna.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.mal.lobna.movieapp.Adapter.HomeAdapter;
import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Fragments.HomeFragment;
import com.mal.lobna.movieapp.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lobna on 12-Nov-16.
 */

public class FetchMovies extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private Context context;
    ProgressDialog progressDialog;
    private String sortBy;
    private SwipeRefreshLayout swipeToRefresh;

    final ArrayList<Movie> movies;

    private final String LOG_TAG = FetchMovies.class.getSimpleName();

    public FetchMovies(Context context, ProgressDialog progressDialog, String sortBy, SwipeRefreshLayout swipeToRefresh) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.sortBy = sortBy;
        this.swipeToRefresh = swipeToRefresh;

        movies = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            String baseURL = "https://api.themoviedb.org/3/discover/movie?";

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
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movie JSON string : " + movieJsonStr);

            return getMovieDataFromJson(movieJsonStr);
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
       /* try {
            if (movieJsonStr == null) {
                return new ArrayList<>(); // TODO a3'yrha l calling fn getFavourites();
            } else
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }*/
        return null;
    }

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
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
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies != null) {
            HomeFragment.homeAdapter.movies.clear();
            for (Movie movie : movies)
                HomeFragment.homeAdapter.movies.add(movie);
        }

        progressDialog.dismiss();
        swipeToRefresh.setRefreshing(false);
    }

    public void saveMovies(ArrayList<Movie> movies) {
        MovieDataSource.getInstance().insertList(movies);
    }
}
