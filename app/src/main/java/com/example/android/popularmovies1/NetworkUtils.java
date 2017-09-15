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
    private static String TRAILER_KEY = "trailer";
    private static String REVIEW_KEY = "review";

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
                    String movieId = m.getString("id");
                    movies.add(new movieDetails(movieTitle, imageTitle, movieOverview, releaseDate, userRating, movieId));
                }
            } catch (JSONException e) {
                Log.e("NetworkUtils", "Problem parsing JSON results", e);
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

    public static ArrayList<ArrayList<String>> getTrailerAndReviews(String movieId){
        ArrayList<String> trailers;
        ArrayList<String> reviews;
        URL trailersURL = buildTrailerOrReviewURL(movieId, TRAILER_KEY);
        /** query api for trailers**/
        String trailerResponse = null;
        try {
            trailerResponse = getResponseFromHttpUrl(trailersURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (trailerResponse != null){
            trailers = extractTrailerKeys(trailerResponse);
        }else{trailers = null;}

        /** query api for Reviews**/
        URL reviewURL = buildTrailerOrReviewURL(movieId, REVIEW_KEY);
        String reviewResponse = null;
        try {
            reviewResponse = getResponseFromHttpUrl(reviewURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (reviewResponse != null){
            reviews = extractReviews(reviewResponse);
        }else{reviews = null;}
        ArrayList<ArrayList<String>> trailersAndReviews = new ArrayList<ArrayList<String>>();
        trailersAndReviews.add(trailers);
        trailersAndReviews.add(reviews);
        return trailersAndReviews;
    }

    //this methods builds a url for querying the api. It takes in the movie id and key to return movie or review URL
    private static URL buildTrailerOrReviewURL(String movieId, String trailerOrreview){
        String baseURL = null;
        if (trailerOrreview.matches(TRAILER_KEY)) {
            baseURL = MOVIES_BASE_URL + movieId + "/videos";
        }else if(trailerOrreview.matches(REVIEW_KEY)){
            baseURL = MOVIES_BASE_URL + movieId + "/reviews";
        }
        Uri trailerUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(trailerUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static ArrayList<String> extractTrailerKeys(String jsonResponse){
        ArrayList<String> trailers = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray trailersArray = jsonObject.getJSONArray("results");

            if(trailersArray.length() != 0 && trailersArray != null) {
                for (int i = 0; i < trailersArray.length(); i++) {
                    JSONObject m = trailersArray.getJSONObject(i);
                    String trailerKey = m.getString("key");
                    trailers.add(trailerKey);
                }
            }else{
                trailers = null;
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing JSON results", e);
        }
        return trailers;
    }

    private static ArrayList<String> extractReviews(String jsonResponse){
        ArrayList<String> reviews = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray reviewsArray = jsonObject.getJSONArray("results");

            if(reviewsArray.length() != 0 && reviewsArray != null) {
                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject m = reviewsArray.getJSONObject(i);
                    String author = m.getString("author");
                    String content = m.getString("content");
                    String review = author + ": \n" + content;
                    reviews.add(review);
                }
            }else{
                reviews = null;
            }
        } catch (JSONException e) {
            Log.e("NetworkUtils", "Problem parsing JSON results", e);
        }
        return reviews;
    }
}