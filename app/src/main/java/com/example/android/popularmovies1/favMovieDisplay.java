package com.example.android.popularmovies1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.popularmovies1.data.favMoviesContract;
import com.example.android.popularmovies1.data.movieDbHelper;

import java.util.ArrayList;

/**
 * Created by Akshay on 04-09-2017.
 */

public class favMovieDisplay {
    private static SQLiteDatabase mDb;
    public static Cursor getAllFav(Context context) {
        movieDbHelper dbHelper = new movieDbHelper(context);
        mDb = dbHelper.getReadableDatabase();
        Uri uri = favMoviesContract.favMoviesEntry.CONTENT_URI;
        Cursor c = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        return c;
    }
    public static ArrayList<movieDetails> extractFromCursor(Cursor cursor){
        ArrayList<movieDetails> favoriteMovies = new ArrayList<movieDetails>();
        if(cursor == null){return null;}
        if (cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.COLUMN_MOVIE_TITLE));
                String posterPath = cursor.getString(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.COLUMN_POSTER_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.COLUMN_OVERVIEW));
                String releaseDate = cursor.getString(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.COLUMN_RELEASE_DATE));
                double rating = cursor.getDouble(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.USER_RATING));
                String movieId = cursor.getString(cursor.getColumnIndex(favMoviesContract.favMoviesEntry.MOVIE_ID));
                favoriteMovies.add(new movieDetails(title, posterPath, overview, releaseDate, rating, movieId));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return favoriteMovies;
    }
}
