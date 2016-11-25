package com.mal.lobna.movieapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mal.lobna.movieapp.Application.MovieApplication;
import com.mal.lobna.movieapp.Data.MovieContract;
import com.mal.lobna.movieapp.Data.MovieDBHandler;
import com.mal.lobna.movieapp.Data.MovieDataSource;
import com.mal.lobna.movieapp.Fragments.HomeFragment;
import com.mal.lobna.movieapp.Fragments.MovieViewFragment;
import com.mal.lobna.movieapp.Models.Movie;
import com.mal.lobna.movieapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MovieViewActivity extends AppCompatActivity {

    private Movie movie;
    private FloatingActionButton favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE));
        setSupportActionBar(toolbar);

        setToolbarImage();

        movie = new Movie();
        movie.setId(getIntent().getExtras().getInt(MovieContract.MovieTable.COLOUMN_ID));
        movie.setPoster_path(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER));
        movie.setOriginal_title(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_ORIGINAL_TITLE));
        movie.setOverview(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_OVERVIEW));
        movie.setVote_average(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_AVERAGE_VOTING));
        movie.setRelease_date(getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_RELEASE_DATE));
        movie.setFavourite(getIntent().getExtras().getBoolean(MovieContract.MovieTable.COLOUMN_MOVIE_FAVOURITE));

        favButton = (FloatingActionButton) findViewById(R.id.favouriteMovie);
        setFavIcon();

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movieViewLayout, new MovieViewFragment(), "MovieViewFragment").commit();
        }*/
    }

    public void setToolbarImage(){
        String baseURL = "http://image.tmdb.org/t/p/";
        String size = "original/";
        String posterPath = getIntent().getExtras().getString(MovieContract.MovieTable.COLOUMN_MOVIE_POSTER);
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.moviePosterImageView);
        Picasso.with(MovieApplication.getMovieApp().getApplicationContext()).load(baseURL + size + posterPath).into(moviePosterImageView);
    }

    public void setFavIcon(){
        if(movie.isFavourite()){
            favButton.setImageResource(android.R.drawable.btn_star_big_on);
        }else {
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
        if(movie.isFavourite()){
            favButton.setImageResource(android.R.drawable.btn_star_big_off);
            movie.setFavourite(false);
        }else{
            favButton.setImageResource(android.R.drawable.btn_star_big_on);
            movie.setFavourite(true);
        }
        MovieDataSource.getInstance().markAsFav(movie);
    }
}
