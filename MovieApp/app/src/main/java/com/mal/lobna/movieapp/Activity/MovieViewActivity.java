package com.mal.lobna.movieapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;

public class MovieViewActivity extends AppCompatActivity {

    private Movie movie;
    private FloatingActionButton favButton;
    private static Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        movie = (Movie) getIntent().getExtras().getSerializable("item");
        arguments = new Bundle();
        arguments.putSerializable("item", movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getOriginal_title());
        setSupportActionBar(toolbar);
        setToolbarImage();

        favButton = (FloatingActionButton) findViewById(R.id.favouriteMovie);
        setFavIcon();
    }

    public void setToolbarImage() {
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "original/";
        String posterPath = movie.getPoster_path();
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageView);
        Picasso.with(MovieApplication.getMovieApp().getApplicationContext()).load(baseURL + size + posterPath).into(moviePosterImageView);
    }

    public void setFavIcon() {
        favButton.setVisibility(View.VISIBLE);
        if (movie.isFavourite()) {
            favButton.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            favButton.setImageResource(android.R.drawable.btn_star_big_off);
        }
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
            startActivity(new Intent(MovieViewActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void favouriteMovie(View view) {
        if (movie.isFavourite()) {
            favButton.setImageResource(android.R.drawable.btn_star_big_off);
            movie.setFavourite(false);
        } else {
            favButton.setImageResource(android.R.drawable.btn_star_big_on);
            movie.setFavourite(true);
        }
        MovieDataSource.getInstance().markAsFav(movie);
    }

    public void moveToUp(View view) {
        NestedScrollView movieViewNestedScrollView = (NestedScrollView) findViewById(R.id.movieViewNestedScrollView);
        movieViewNestedScrollView.smoothScrollTo(0, 0);
        expandToolBar();
    }

    public void expandToolBar() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        behavior.setTopAndBottomOffset(0);
    }

    public static Bundle getArguments() {
        return arguments;
    }
}
