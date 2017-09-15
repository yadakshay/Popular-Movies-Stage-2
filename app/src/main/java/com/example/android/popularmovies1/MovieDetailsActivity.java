package com.example.android.popularmovies1;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.data.favMoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.popularmovies1.MainActivity.passDetailsObject;

public class MovieDetailsActivity extends AppCompatActivity implements
        LoaderCallbacks<ArrayList<ArrayList<String>>>{
    private static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static String RELEASE_DATE = "Release Date: ";
    private static String USER_RATING ="User Rating: ";
    private static final int DETAILS_LOADER_ID = 1004;
    private static final String MOVIE_ID_KEY = "movieIdKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView textView = (TextView) findViewById(R.id.movieName);
        textView.setText(passDetailsObject.getMovieTitle());

        ImageView moviePoster = (ImageView) findViewById(R.id.posterView);
        String imagePath = POSTER_BASE_URL + passDetailsObject.getImagePath();
        Picasso.with(this).load(imagePath).into(moviePoster);

        TextView dateDisplay = (TextView) findViewById(R.id.dateView);
        dateDisplay.setText(RELEASE_DATE + passDetailsObject.getReleaseDate());

        TextView userRating = (TextView) findViewById(R.id.userRatingView);
        userRating.setText(USER_RATING + String.valueOf(passDetailsObject.getUserRating()));

        TextView synopsisView = (TextView) findViewById(R.id.synopsisView);
        synopsisView.setText(passDetailsObject.getMovieOverview());

        LoaderCallbacks<ArrayList<ArrayList<String>>> callback = MovieDetailsActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(MOVIE_ID_KEY, passDetailsObject.getMovieId());
        getSupportLoaderManager().initLoader(DETAILS_LOADER_ID, bundleForLoader, callback);
    }

    public void addToFav(View view) {
        ContentValues cv = new ContentValues();
        cv.put(favMoviesContract.favMoviesEntry.COLUMN_MOVIE_TITLE, passDetailsObject.getMovieTitle());
        cv.put(favMoviesContract.favMoviesEntry.COLUMN_POSTER_PATH, passDetailsObject.getImagePath());
        cv.put(favMoviesContract.favMoviesEntry.COLUMN_OVERVIEW, passDetailsObject.getMovieOverview());
        cv.put(favMoviesContract.favMoviesEntry.COLUMN_RELEASE_DATE, passDetailsObject.getReleaseDate());
        cv.put(favMoviesContract.favMoviesEntry.USER_RATING, passDetailsObject.getUserRating());
        cv.put(favMoviesContract.favMoviesEntry.MOVIE_ID, passDetailsObject.getMovieId());
        Uri queryUri = favMoviesContract.favMoviesEntry.CONTENT_URI;
        queryUri = queryUri.buildUpon().appendPath(passDetailsObject.getMovieTitle()).build();
        Cursor c = getContentResolver().query(queryUri, null, null, null, null);
        if(c !=null && c.getCount()>0){
            Uri uri = favMoviesContract.favMoviesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(passDetailsObject.getMovieTitle()).build();
            int deleted = getContentResolver().delete(uri, null, null);
            if(deleted>0){
                Toast.makeText(this, "Movie Deleted from favorites", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Unable to Delete from favorites", Toast.LENGTH_SHORT).show();
            }
        } else {
                Uri uri = getContentResolver().insert(favMoviesContract.favMoviesEntry.CONTENT_URI, cv);
                if (uri != null) {
                    Toast.makeText(this, "Sucess - Movie added to favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public Loader<ArrayList<ArrayList<String>>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<ArrayList<String>>>(this) {
            //variable to cache data
            ArrayList<ArrayList<String>> mData = null;
            String movieId = args.getString(MOVIE_ID_KEY);
            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }
            @Override
            public ArrayList<ArrayList<String>> loadInBackground() {
                ArrayList<ArrayList<String>> trailerAndReviewData = new ArrayList<ArrayList<String>>();
                trailerAndReviewData = NetworkUtils.getTrailerAndReviews(movieId);
                return trailerAndReviewData;
            }
            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<ArrayList<String>> data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArrayList<String>>> loader, ArrayList<ArrayList<String>> data) {
        if (data != null) {
            if (data.size() != 0) {
                ArrayList<String> trailers = data.get(0);
                ArrayList<String> reviews = data.get(1);
                TextView textView = (TextView) findViewById(R.id.movieReviews);
                if (reviews != null) {
                    if (reviews.size() != 0) {
                        for (int i = 0; i < reviews.size(); i++) {
                            textView.append(reviews.get(i) + "\n*******************" + "\n\n");
                        }
                    }
                }else {
                    textView.setText(getString(R.string.noReview));
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArrayList<String>>> loader) {

    }
}
