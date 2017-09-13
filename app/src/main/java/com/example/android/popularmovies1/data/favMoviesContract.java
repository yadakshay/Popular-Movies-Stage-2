package com.example.android.popularmovies1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Akshay on 30-07-2017.
 */

public class favMoviesContract {

    public static final String AUTHORITY = "com.example.android.popularmovies1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASK = "favMovies";
    //constructor
    private favMoviesContract(){}

    public static final class favMoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();
        public static final String TABLE_NAME = "favMovies";
        public static final String COLUMN_MOVIE_TITLE = "movieName";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "rDate";
        public static final String USER_RATING = "userRating";
        public static final String MOVIE_ID = "movieId";
    }
}
