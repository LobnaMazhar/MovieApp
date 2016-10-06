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
 * Created by Lobna on 05-Oct-16.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    HomeAdapter homeAdapter;

    String apiKey = ""; // TODO add your own API key here !!

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
               // TODO navigate to next page for details about the movie
            //    Intent goToDetailActivity = new Intent(getActivity(), DetailActivity.class);
             //   goToDetailActivity.putExtra("data", moviesEntries.get(position));
              //  startActivity(goToDetailActivity);
            }
        });

        return rootView;
    }

    public class getMovies extends AsyncTask<Void, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {
                String baseURL = "https://api.themoviedb.org/3/discover/movie?";

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortBy = sharedPreferences.getString(getString(R.string.sortMoviesBy_prefKey), getResources().getString(R.string.sortMoviesBy_defaultPrefValue));

                Uri builtUri = Uri.parse(baseURL).buildUpon().appendQueryParameter("api_key", apiKey).appendQueryParameter("sort_by", sortBy).build();
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
                return getMovieDataFromJson(movieJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<>();

            JSONObject reader = new JSONObject(movieJsonStr);
            JSONArray results = reader.getJSONArray("results");

            for(int i=0; i<results.length(); ++i){
                JSONObject movieData = results.getJSONObject(i);

                Movie movieObject = new Movie();

                movieObject.id = movieData.getString("id");
      //          movieObject.original_title = movieData.getString("original_title");
       //         movieObject.overview = movieData.getString("overview");
        //        movieObject.popularity = movieData.getString("popularity");
                movieObject.poster_path = movieData.getString("poster_path");
         //       movieObject.release_date = movieData.getString("release_date");
                movieObject.title = movieData.getString("title");
         //       movieObject.vote_average = movieData.getString("vote_average");
           //     movieObject.vote_count = movieData.getString("vote_count");

               // Log.v(LOG_TAG, "current iteration   i = " + Integer.toString(i) +  "    current ID : " + movieData.getString("id") + "   current poster+path : " + reader.getString("poster_path"));

                movies.add(movieObject);
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies != null){
                homeAdapter.clear();
                for(Movie movie : movies)
                    homeAdapter.add(movie);
            }
        }
    }

    public void updateMovies(){
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
       // String chosen = sharedPreferences.getString(getString(R.string.pref_location_key), getResources().getString(R.string.pref_location_default));
        new getMovies().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }
}
