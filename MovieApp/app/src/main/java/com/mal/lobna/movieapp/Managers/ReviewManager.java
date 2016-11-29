package com.mal.lobna.movieapp.Managers;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.BuildConfig;
import com.mal.lobna.movieapp.Listeners.ReviewListener;
import com.mal.lobna.movieapp.Models.Review;
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
 * Created by Lobna on 25-Nov-16.
 */

public class ReviewManager {

    private static ReviewManager reviewManager;

    private ArrayList<Review> reviews;

    Handler handler;

    public static ReviewManager getInstance() {
        if (reviewManager == null) {
            reviewManager = new ReviewManager();
            reviewManager.handler = new Handler();
        }
        return reviewManager;
    }

    public void getReviews(final int id, final ReviewListener reviewListener) {
        if (!Utilities.networkConnectivity()) {
            reviewListener.onFail(new Exception("No internet connection!"));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;

                    BufferedReader bufferedReader = null;

                    String reviewJSONStr;

                    try {
                        String baseURL = MovieApplication.getMovieApp().getApplicationContext().getString(R.string.baseMovieURL) +
                                String.valueOf(id) + "/reviews?";

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
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line + "\n");
                        }

                        if (buffer.length() == 0) {
                            // Stream was empty.  No point in parsing.
                            return;
                        }
                        reviewJSONStr = buffer.toString();

                        getReviewsFromJSONStr(reviewJSONStr);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                reviewListener.onDownloadFinished(reviews);
                            }
                        });
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }).start();
        }
    }

    private void getReviewsFromJSONStr(String reviewJSONStr) {
        reviews = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(reviewJSONStr);
            JSONArray results = reader.getJSONArray("results");
            for (int i = 0; i < results.length(); ++i) {
                JSONObject result = results.getJSONObject(i);
                Review reviewObject = new Review();

                reviewObject.setReview(result.getString("content"));

                reviews.add(reviewObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
