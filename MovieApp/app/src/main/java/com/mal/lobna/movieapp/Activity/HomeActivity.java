package com.mal.lobna.movieapp.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Listeners.CallbackListener;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements CallbackListener {

    private static Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.action_settings) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(final Movie item) {
        // Tablet UI
        if (findViewById(R.id.movieViewLayout) != null) {
            View currentView = findViewById(R.id.movieViewLayout);

            String baseURL = "http://image.tmdb.org/t/p/";
            String size = "original/";
            String posterPath = item.getPoster_path();
            ImageView moviePosterImageView = (ImageView) currentView.findViewById(R.id.moviePosterImageView);
            Picasso.with(MovieApplication.getMovieApp().getApplicationContext()).load(baseURL + size + posterPath).into(moviePosterImageView);

            final FloatingActionButton moveToUpButton;
            moveToUpButton = (FloatingActionButton) currentView.findViewById(R.id.moveToUp);
            moveToUpButton.setVisibility(View.VISIBLE);

            final FloatingActionButton favButton;
            favButton = (FloatingActionButton) currentView.findViewById(R.id.favouriteMovie);
            favButton.setVisibility(View.VISIBLE);
            if (item.isFavourite()) {
                favButton.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                favButton.setImageResource(android.R.drawable.btn_star_big_off);
            }
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isFavourite()) {
                        favButton.setImageResource(android.R.drawable.btn_star_big_off);
                        item.setFavourite(false);
                    } else {
                        favButton.setImageResource(android.R.drawable.btn_star_big_on);
                        item.setFavourite(true);
                    }
                    MovieDataSource.getInstance().markAsFav(item);
                }
            });

            arguments = new Bundle();
            arguments.putSerializable("item", item);

            getSupportFragmentManager().findFragmentById(R.id.movieViewFragment).onResume();
            getSupportFragmentManager().findFragmentById(R.id.trailersFragment).onResume();
            getSupportFragmentManager().findFragmentById(R.id.reviewFragment).onResume();

        } else {
            Intent goToMovieViewActivity = new Intent(this, MovieViewActivity.class);
            goToMovieViewActivity.putExtra("item", item);
            startActivity(goToMovieViewActivity);
        }
    }

    public void moveToUp(View view) {
        NestedScrollView movieViewNestedScrollView = (NestedScrollView) findViewById(R.id.movieViewNestedScrollView);
        movieViewNestedScrollView.smoothScrollTo(0, 0);
    }

    public static Bundle getArguments() {
        return arguments;
    }
}
