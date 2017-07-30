package com.example.android.popularmovies1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import static com.example.android.popularmovies1.MainActivity.passDetailsObject;

public class MovieDetailsActivity extends AppCompatActivity {
    private static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static String RELEASE_DATE = "Release Date: ";
    private static String USER_RATING ="User Rating: ";
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
    }
}
