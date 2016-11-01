package com.mal.lobna.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieViewActivity extends AppCompatActivity {

    public MovieDBHandler movieDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("Original title"));
        setSupportActionBar(toolbar);

        movieDBHandler = new MovieDBHandler(this);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if(itemID == R.id.action_settings){
            startActivity(new Intent(MovieViewActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "original/";
        String posterPath = getIntent().getExtras().getString("Movie Poster");
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageView);
        Picasso.with(this).load(baseURL + size + posterPath).into(moviePosterImageView);

        String overview = getIntent().getExtras().getString("Overview");
        TextView movieOverviewTextView = (TextView) findViewById(R.id.movieOverviewTextView);
        movieOverviewTextView.setText(overview);

        String voteAverage = getIntent().getExtras().getString("Average Voting");
        TextView movieAverageVoteTextView = (TextView) findViewById(R.id.movieAverageVoteTextView);
        movieAverageVoteTextView.setText("The average votes for this movie is " + voteAverage);

        String releaseDate = getIntent().getExtras().getString("Release Date");
        TextView movieReleaseDateTextView = (TextView) findViewById(R.id.movieReleaseDateTextView);
        movieReleaseDateTextView.setText("This movie was released on " + releaseDate);
    }

    public void favouriteMovie(View view){
        Bundle bundle = getIntent().getExtras();
        movieDBHandler.favouriteMovie(bundle.getString("Original title"), bundle.getString("Movie Poster"), bundle.getString("Overview"), bundle.getString("Average Voting"), bundle.getString("Release Date"));
    }
}
