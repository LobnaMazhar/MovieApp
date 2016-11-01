package com.mal.lobna.movieapp;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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

import com.mal.lobna.movieapp.MovieDBHandler;

/**
 * Created by Lobna on 05-Oct-16.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    HomeAdapter homeAdapter;

    private final String LOG_TAG = getMovies.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ArrayList<Movie> moviesEntries = new ArrayList<Movie>();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homeAdapter = new HomeAdapter(getActivity(), moviesEntries);
        GridView moviesHomeGridView = (GridView) rootView.findViewById(R.id.moviesHomeGridView);
        moviesHomeGridView.setAdapter(homeAdapter);
        moviesHomeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToMovieViewActivity = new Intent(getActivity(), MovieViewActivity.class);
                goToMovieViewActivity.putExtra("Original title", moviesEntries.get(position).getOriginal_title());
                goToMovieViewActivity.putExtra("Movie Poster", moviesEntries.get(position).getPoster_path());
                goToMovieViewActivity.putExtra("Overview", moviesEntries.get(position).getOverview());
                goToMovieViewActivity.putExtra("Average Voting", moviesEntries.get(position).getVote_average());
                goToMovieViewActivity.putExtra("Release Date", moviesEntries.get(position).getRelease_date());
                startActivity(goToMovieViewActivity);
            }
        });

        return rootView;
    }

    public class getMovies extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {
                String baseURL = "https://api.themoviedb.org/3/discover/movie?";

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortBy = sharedPreferences.getString(getString(R.string.sortMoviesBy_prefKey), getResources().getString(R.string.sortMoviesBy_defaultPrefValue));

                if (!sortBy.equals("favourites")) {
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
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                movieJsonStr = null;
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
            try {
                if (movieJsonStr == null) {
                    return new ArrayList<>(); // TODO a3'yrha l calling fn getFavourites();
                } else
                    return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<>();

            JSONObject reader = new JSONObject(movieJsonStr);
            JSONArray results = reader.getJSONArray("results");

            for (int i = 0; i < results.length(); ++i) {
                JSONObject movieData = results.getJSONObject(i);

                Movie movieObject = new Movie();

                movieObject.id = movieData.getString("id");
                movieObject.original_title = movieData.getString("original_title");
                movieObject.poster_path = movieData.getString("poster_path");
                movieObject.overview = movieData.getString("overview");
                movieObject.vote_average = movieData.getString("vote_average");
                movieObject.release_date = movieData.getString("release_date");
                // movieObject.popularity = movieData.getString("popularity");
                // movieObject.title = movieData.getString("title");
                // movieObject.vote_count = movieData.getString("vote_count");

                // Log.v(LOG_TAG, "current iteration   i = " + Integer.toString(i) +  "    current ID : " + movieData.getString("id") + "   current poster+path : " + reader.getString("poster_path"));

                movies.add(movieObject);
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                homeAdapter.clear();
                for (Movie movie : movies)
                    homeAdapter.add(movie);
            }
        }
    }

    public void updateMovies() {
        new getMovies().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }
}
