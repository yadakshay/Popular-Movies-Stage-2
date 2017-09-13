package com.example.android.popularmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies1.data.favMoviesContract.favMoviesEntry.TABLE_NAME;

/**
 * Created by Akshay on 10-09-2017.
 */


public class movieDbContentProvider extends ContentProvider {

    public static final int MOVIE = 100;
    public static final int MOVIE_UPDATE = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */;
        uriMatcher.addURI(favMoviesContract.AUTHORITY, favMoviesContract.PATH_TASK, MOVIE);
        uriMatcher.addURI(favMoviesContract.AUTHORITY, favMoviesContract.PATH_TASK + "/*", MOVIE_UPDATE);
        return uriMatcher;
    }

    private movieDbHelper mMovieDbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new movieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case MOVIE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_UPDATE:
                String movieName = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME,
                        null,
                        favMoviesContract.favMoviesEntry.COLUMN_MOVIE_TITLE + " = ?",
                        new String[]{movieName},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, cv);
                if(id > 0)
                {
                    returnUri = ContentUris.withAppendedId(favMoviesContract.favMoviesEntry.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        //Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int movieDeleted;
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIE_UPDATE:
                // Get the task ID from the URI path
                String movieName = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                movieDeleted = db.delete(TABLE_NAME, favMoviesContract.favMoviesEntry.COLUMN_MOVIE_TITLE + " = ?", new String[]{movieName});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }        // Notify the resolver of a change and return the number of items deleted
        if (movieDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
