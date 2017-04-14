package com.kumar.prince.popularmovie;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kumar.prince.popularmovie.adapter.MovieAdapter;
import com.kumar.prince.popularmovie.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    /*For Log */
    private final String TAG=getClass().getName();

    /*Array Of Image Url*/
    private String[] imgUrl = new String[20];

    /*Grid View for movie Poster*/
    private GridView mGridView;

    /*Menu option where user can change from Toprated movie to popular movie and vise versa */
    private Menu mMenu;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerView;

    private MovieAdapter movieAdapter;

    int urlType=0;

    private JSONArray movieDetails;
    Context context;

    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState;


    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
//        Log.e(TAG,"\nOn Create");
        context=MainActivity.this;
        /*Get referance of grid view*/
       // mGridView=(GridView) findViewById(R.id.gridView);
        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview_moviecast);

        // This TextView is used to display errors and will be hidden if there are no errors
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        /*Checking the Oritation of screen and set the grid view*/
      /*  if(getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            //do work for landscape screen mode.
            layoutManager = new GridLayoutManager(this,4);
        }
        else if(getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            //Do work for portrait screen mode.
            layoutManager = new GridLayoutManager(this,2);

        }
*/
        layoutManager = new GridLayoutManager(this,2);
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        Log.e("TAG","\nBefore Create Adapter");
        movieAdapter =new MovieAdapter(context);
        Log.e("TAG","\nBefore Set Adapter");
        mRecyclerView.setAdapter(movieAdapter);
        // This TextView is used to display errors and will be hidden if there are no errors
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void loadMovieData(){

        Log.e(TAG,"\nLoading Data");
        showMovieDataView();
        new FetchMovieDataTask().execute(urlType);


    }

    /*Create Menu for selecting for popular movie or High Rating movie*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        System.out.print("\nitemThatWasClickedId"+itemThatWasClickedId);
        if (itemThatWasClickedId == R.id.action_most_popular) {
            //makeGithubSearchQuery();
            Log.d(TAG,"Most Popular");
            urlType=0;
            movieAdapter.setMovierURLData(null);
            loadMovieData();
            return true;
        }else if (itemThatWasClickedId == R.id.action_top_rated){
            Log.d(TAG,"Top Rated");
            urlType=1;
            movieAdapter.setMovierURLData(null);
            loadMovieData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make the View for the Movie data visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }






    public class FetchMovieDataTask extends AsyncTask<Integer, Void, String[]> {


        @Override
        protected String[] doInBackground(Integer... integers) {
            if (integers.length == 0) {
                Log.d(TAG,"Integer Length"+integers.length);
                return null;
            }
            Log.e(TAG,"Integer Length");
            URL movieRequestURL = NetworkUtils.buildUrl(getApplicationContext(),integers[0]);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);
                Log.d(TAG,"\nResponce:- "+jsonWeatherResponse);
                if (jsonWeatherResponse != null) {
                    JSONObject movie = new JSONObject(jsonWeatherResponse);
                    movieDetails = movie.getJSONArray("results");
                    for (int i = 0; i < movieDetails.length(); i++) {
                        JSONObject temp_mov = movieDetails.getJSONObject(i);
                        imgUrl[i] = context.getResources().getString(R.string.poster_url) + temp_mov.getString("poster_path");
                    }
                } else
                    return null;

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (IOException e1) {
                e1.printStackTrace();
            }


            Log.d(TAG,"\nResponce:- "+imgUrl.toString());
            return imgUrl;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (strings != null) {
                // showWeatherDataView();
                movieAdapter.setMovierURLData(strings);
            } else {
                showErrorMessage();
            }
        }


    }


}
