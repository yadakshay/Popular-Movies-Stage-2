package com.example.android.popularmovies1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Akshay on 09-07-2017.
 * defines a custom array adapter
 */

public class customAdapter extends ArrayAdapter<movieDetails> {

    final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public customAdapter(Context context, ArrayList<movieDetails> movieList){
        super(context, 0, movieList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View gridItemView = convertView;
        if(gridItemView == null) {
            //inflate a new view
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        movieDetails currentMovie = getItem(position); //get reference to the current item
        ImageView movieImage = (ImageView) gridItemView.findViewById(R.id.moviePosterHolder); //get reference to the image view

        String imageID = currentMovie.getImagePath();
        String imagePath = getImagePath(imageID);
        Picasso.with(getContext()).load(imagePath).into(movieImage);
        return gridItemView;
    }

    private String getImagePath(String imageId){
        String imagePath = POSTER_BASE_URL + imageId;
        return imagePath;
    }
}
