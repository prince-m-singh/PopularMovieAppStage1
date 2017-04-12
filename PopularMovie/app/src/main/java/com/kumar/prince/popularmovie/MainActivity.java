package com.kumar.prince.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    /*For Log */
    private final String TAG=getClass().getName();

    /*Grid View for movie Poster*/
    private GridView mGridView;

    /*Menu option where user can change from Toprated movie to popular movie and vise versa */
    private Menu mMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Get referance of grid view*/
        mGridView=(GridView) findViewById(R.id.gridView);


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
        if (itemThatWasClickedId == R.id.action_most_popular) {
            //makeGithubSearchQuery();
            Log.d(TAG,"Most Popular");
            return true;
        }else if (itemThatWasClickedId == R.id.action_top_rated){
            Log.d(TAG,"Top Rated");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
