package com.mal.lobna.movieapp.Managers;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.BuildConfig;
import com.mal.lobna.movieapp.Models.Trailer;
import com.mal.lobna.movieapp.Listeners.TrailerListener;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lobna on 18-Nov-16.
 */

public class TrailerManager {

    private static TrailerManager trailerManager;

    private ArrayList<Trailer> trailers;

    Handler handler;

    public static TrailerManager getInstance() {
        if (trailerManager == null) {
            trailerManager = new TrailerManager();
            trailerManager.handler = new Handler();
        }
        return trailerManager;
    }

    public void getTrailers(final int id, final TrailerListener trailerListener) {
        if (!Utilities.networkConnectivity()) {
            trailerListener.onFail(new Exception("No internet connection"));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader;
                    String trailerJSONStr;
                    try {
                        String baseURL = MovieApplication.getMovieApp().getApplicationContext().getString(R.string.baseMovieURL) +
                                String.valueOf(id) +
                                "/videos?";

                        Uri builtUri = Uri.parse(baseURL).buildUpon().appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY).build();
                        URL url = new URL(builtUri.toString());

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
                        trailerJSONStr = buffer.toString();

                        getTrailersFromJSON(trailerJSONStr);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                trailerListener.onDownloadFinished(trailers);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        urlConnection.disconnect();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        }
    }

    public void getTrailersFromJSON(String trailerJSONStr) {
        trailers = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(trailerJSONStr);
            JSONArray result = reader.getJSONArray("results");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject trailerData = result.getJSONObject(i);

                Trailer trailerObject = new Trailer();

                trailerObject.setId(trailerData.getString("id"));
                trailerObject.setName(trailerData.getString("name"));
                trailerObject.setKey(trailerData.getString("key"));

                trailers.add(trailerObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
