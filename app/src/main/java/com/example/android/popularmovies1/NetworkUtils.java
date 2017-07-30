package com.example.android.popularmovies1;

/**
 * Created by Akshay on 04-07-2017.
 */


import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network and extract data form JSON format.
 */
public class NetworkUtils {

    final static String PARAM_QUERY = "api_key";
    final static String API_KEY = ""; //Enter your API key here
    private static String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";

/* this method build the query url*/
    public static URL buildUrl(String sortBy) {
        String queryURL = MOVIES_BASE_URL + sortBy;
        Uri builtUri = Uri.parse(queryURL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

/* method gets data from network  and returns it in form of an array list*/
    public static ArrayList<movieDetails> getMovies(String url) {

        URL queryURL = NetworkUtils.buildUrl(url);
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<movieDetails> movies = NetworkUtils.extractFromJSON(jsonResponse);
        return movies;
    }

/*exacts data from the json format*/
    public static ArrayList<movieDetails> extractFromJSON(String jString){
        ArrayList<movieDetails> movies = new ArrayList<>();

        if(jString!= null) {
            try {
                JSONObject jsonObject = new JSONObject(jString);
                JSONArray moviesArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject m = moviesArray.getJSONObject(i);
                    String movieTitle = m.getString("original_title");
                    String imageTitle = m.getString("poster_path");
                    String movieOverview = m.getString("overview");
                    String releaseDate = m.getString("release_date");
                    double userRating = m.getDouble("vote_average");
                    movies.add(new movieDetails(movieTitle, imageTitle, movieOverview, releaseDate, userRating));
                }
            } catch (JSONException e) {
                Log.e("QueryUtils", "Problem parsing JSON results", e);
            }
            return movies;
        }
        else{return null;}
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}