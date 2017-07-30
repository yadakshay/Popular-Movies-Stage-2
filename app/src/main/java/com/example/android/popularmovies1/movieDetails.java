package com.example.android.popularmovies1;

/**
 * Created by Akshay on 08-07-2017.
 */

public class movieDetails {

    private String movieTitle, imagePath, movieOverview, releaseDate;
    private double userRating;

    // constructor

    public movieDetails(String title, String imageFetch, String overview, String dateRel, double uRatings){
        movieTitle = title;
        imagePath = imageFetch;
        movieOverview = overview;
        releaseDate = dateRel;
        userRating = uRatings;
    }

    //return methods

    public String getMovieTitle(){return movieTitle;}
    public String getImagePath(){return imagePath;}
    public String getMovieOverview(){return movieOverview;}
    public String getReleaseDate(){return releaseDate;}
    public double getUserRating(){return userRating;}
}
