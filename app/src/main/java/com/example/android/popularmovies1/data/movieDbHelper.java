package com.example.android.popularmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Akshay on 10-08-2017.
 */

public class movieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favMovie.db";
    private static final int DATABASE_VERSION = 2;

    public movieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAV_MOVIE_TABLE = "CREATE TABLE " +
                favMoviesContract.favMoviesEntry.TABLE_NAME + " (" +
                favMoviesContract.favMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                favMoviesContract.favMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                favMoviesContract.favMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                favMoviesContract.favMoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                favMoviesContract.favMoviesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                favMoviesContract.favMoviesEntry.USER_RATING + " TEXT" +
                favMoviesContract.favMoviesEntry.MOVIE_ID + " TEXT" +
                ");";

        db.execSQL(SQL_CREATE_FAV_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + favMoviesContract.favMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
