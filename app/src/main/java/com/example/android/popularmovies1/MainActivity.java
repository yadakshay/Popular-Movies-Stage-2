package com.example.android.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView movieGrid;
    private TextView mEmptyStateTextView;
    public static movieDetails passDetailsObject;
    private static String sortBy = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* check if there is a network connection*/
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected) {

            new movieQueryTask().execute(sortBy);


        }else{
            ProgressBar loading = (ProgressBar) findViewById(R.id.loading_spinner);
            loading.setVisibility(View.GONE);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

    }

    private class movieQueryTask extends AsyncTask<String, Void, ArrayList<movieDetails>> {

        @Override
        protected ArrayList<movieDetails> doInBackground(String... params) {
            String queryURLString = params[0];
            ArrayList<movieDetails> capturedList = null;
            capturedList = NetworkUtils.getMovies(queryURLString); // returns the movie list in form of a custom movieDetails class
            return capturedList;
        }

        @Override
        protected void onPostExecute(ArrayList<movieDetails> s) {

            ProgressBar loading = (ProgressBar) findViewById(R.id.loading_spinner);
            final ArrayList<movieDetails> movieList = s;
            loading.setVisibility(View.GONE);

            if (s != null && !s.equals("")) {
                movieGrid = (GridView) findViewById(R.id.movieGrid);
                customAdapter adapter = new customAdapter(MainActivity.this, s);
                movieGrid.setAdapter(adapter);

                movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick (AdapterView < ? > parent, View view, int position, long id){
                    movieDetails currentMovie = movieList.get(position);

                        Context context = MainActivity.this;
                        Class destinationActivity = MovieDetailsActivity.class;
                        Intent intent = new Intent(context, destinationActivity);
                        passDetailsObject = currentMovie; //global public variable to pass movie details to other activity
                        startActivity(intent);
                }

                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mostPopular) {
            sortBy = "popular";
            new movieQueryTask().execute(sortBy);
            return true;
        }
        if (id == R.id.mostRated) {
            sortBy = "top_rated";
            new movieQueryTask().execute(sortBy);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
