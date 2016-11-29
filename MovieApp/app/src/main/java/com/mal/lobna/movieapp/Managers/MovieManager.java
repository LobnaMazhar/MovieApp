package com.mal.lobna.movieapp.Managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.BuildConfig;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.Listeners.MoviesListener;
import com.mal.lobna.movieapp.R;
import com.mal.lobna.movieapp.Utilities.Utilities;

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
 * Created by Lobna on 18-Nov-16.
 */

public class MovieManager {

    private static String LOG_TAG = MovieManager.class.getSimpleName();

    private static MovieManager movieManager;
    Handler handler;

    private static ArrayList<Movie> movies = new ArrayList<>();

    public static MovieManager getInstance() {
        if (movieManager == null) {
            movieManager = new MovieManager();
            movieManager.handler = new Handler();
        }
        return movieManager;
    }

    public void getMovies(final MoviesListener moviesListener) {
        Context context = MovieApplication.getMovieApp().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String sortBy = sharedPreferences.getString(context.getString(R.string.sortMoviesBy_prefKey), context.getResources().getString(R.string.sortMoviesBy_defaultPrefValue));
        if (sortBy.equals(context.getString(R.string.favourites_prefValueID))) {
            movies = MovieDataSource.getInstance().getFavourites();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    moviesListener.onDownloadFinished(movies);
                }
            });
        } else if (!Utilities.networkConnectivity()) {
            moviesListener.onFail(new Exception("No internet connection"));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;

                    String movieJsonStr = null;
                    try {
                        String baseURL = MovieApplication.getMovieApp().getApplicationContext().getString(R.string.baseDiscoverURL);
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

                        // Add downloaded movies to database
                        saveMovies(movies);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                moviesListener.onDownloadFinished(movies);
                            }
                        });

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
                }
            }).start();
        }
    }

    private void getMovieDataFromJson(String movieJsonStr) throws JSONException {
        JSONObject reader = new JSONObject(movieJsonStr);
        JSONArray results = reader.getJSONArray("results");

        for (int i = 0; i < results.length(); ++i) {
            JSONObject movieData = results.getJSONObject(i);

            Movie movieObject = new Movie();

            movieObject.setId(Integer.valueOf(movieData.getString("id")));
            movieObject.setOriginal_title(movieData.getString("original_title"));
            movieObject.setPoster_path(movieData.getString("poster_path"));
            movieObject.setOverview(movieData.getString("overview"));
            movieObject.setVote_average(movieData.getString("vote_average"));
            movieObject.setRelease_date(movieData.getString("release_date"));
            movieObject.setFavourite(false);

            movies.add(movieObject);
        }
    }

    public void saveMovies(ArrayList<Movie> movies) {
        MovieDataSource.getInstance().insertList(movies);
    }

    public static ArrayList<Movie> getMoviesList(){
        return movies;
    }
}
