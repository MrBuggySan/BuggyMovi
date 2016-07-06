package com.example.andreibuiza.buggymovi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements FrontMenuFragment.OnButtonSelectedListener,
        MovieGridFragment.OnPosterSelectedListener, AdapterView.OnItemSelectedListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private theMovieDB_API_response API_full_response;
    private int currentCategoryID;
    private boolean tabletLayout;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Save the current category
        savedInstanceState.putInt(MainActivity.class.getSimpleName(), currentCategoryID);
        Log.d(LOG_TAG, "onSaveInstanceState is called");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState is called");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        LinearLayout contaierlayout = (LinearLayout) findViewById(R.id.container);
        LinearLayout tabletlayout = (LinearLayout) contaierlayout.findViewById(R.id.TabletLayout);
        Log.d(LOG_TAG, (String) contaierlayout.getTag() );
        //Determine what version of layout we have
       if( tabletlayout == null ) {
           //phone layout is used
           //If true this means this Activity is a new instance, we are not using a previous instance
           tabletLayout=false;
           if (savedInstanceState == null) {

               //TODO: get the last selected category from sharedPreferences
               MovieGridFragment gridViewFragment = new MovieGridFragment();
               currentCategoryID = R.id.popButton;
               int buttonID= currentCategoryID;
               //set the argument of the MovieGridFragment
               Bundle args = new Bundle();
               args.putInt(getString(R.string.menuToGridKey), buttonID);
               gridViewFragment.setArguments(args);

               getSupportFragmentManager().beginTransaction().add(R.id.TabletLayoutGridView,gridViewFragment).commit();
//
               Log.d(LOG_TAG, "Activity onCreate, the default category is popular movies");
           }else{
               Log.d(LOG_TAG, "savedInstanceState is not null");
           }

       }else{
           //tablet layout is used
           tabletLayout=true;
           if(savedInstanceState == null ){
               MovieGridFragment gridViewFragment = new MovieGridFragment();
               currentCategoryID = R.id.popButton;
               Bundle args = new Bundle();
               args.putInt(getString(R.string.menuToGridKey), currentCategoryID);
               gridViewFragment.setArguments(args);

               //add the gridView to the left side of the layout
               getSupportFragmentManager().beginTransaction().add(R.id.TabletLayoutGridView,gridViewFragment).commit();
           }


       }



    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
        MovieGridFragment gridViewFragment = new MovieGridFragment();
        int buttonID= currentCategoryID;

        //set the argument of the MovieGridFragment
        Bundle args = new Bundle();
        args.putInt(getString(R.string.menuToGridKey), buttonID);
        gridViewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.TabletLayoutGridView, gridViewFragment);

        transaction.commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        //TODO: Save the category selected inside sharedPrefences
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.About) {
            Intent intent = new Intent (this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.ClearFavourites){
            //clear the favourites list
            SharedPreferences mPrefs = getSharedPreferences(getString(R.string.favouritelistKEY),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.clear();
            prefsEditor.commit();
            //TODO: if we are on favourites, refresh the page by calling a new favourites gridView
            if(currentCategoryID == R.id.favouriteButton){
                MovieGridFragment gridViewFragment = new MovieGridFragment();

                Bundle args = new Bundle();
                args.putInt(getString(R.string.menuToGridKey), currentCategoryID);
                gridViewFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.TabletLayoutGridView, gridViewFragment);

                transaction.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is activated when one of the items in the spinner is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        int buttonID = 0;
        switch (pos){
            case 0: buttonID = R.id.popButton;
                break;
            case 1: buttonID = R.id.topButton;
                break;
            case 2: buttonID = R.id.nowPlayingButton;
                break;
            case 3: buttonID = R.id.upcomingButton;
                break;
            case 4: buttonID = R.id.favouriteButton;
                break;
        }

        if(buttonID != currentCategoryID){
            //Why can't I call OnButtonSelected in this function !?!
            //OnButtonSelected

            //This is the copy of OnButtonSelected
            MovieGridFragment gridViewFragment = new MovieGridFragment();
            currentCategoryID = buttonID;
            //set the argument of the MovieGridFragment
            Bundle args = new Bundle();
            args.putInt(getString(R.string.menuToGridKey), buttonID);
            gridViewFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.TabletLayoutGridView, gridViewFragment);
            //transaction.addToBackStack(null);
            transaction.commit();



        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void OnPosterSelected(Movie_element movieSelected){

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.movieSelectedkey), movieSelected);


        if(tabletLayout){
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.TabletLayoutDetailView, detailFragment);
            transaction.commit();
        }else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.class.getSimpleName(), args);
            startActivity(intent);
        }




    }


    @Override
    //TODO: The detail fragment will be activated when 'latest movie' is selected by the user
    public void OnButtonSelected(int buttonID){
        MovieGridFragment gridViewFragment = new MovieGridFragment();

        currentCategoryID = buttonID;

        //set the argument of the MovieGridFragment
        Bundle args = new Bundle();
        args.putInt(getString(R.string.menuToGridKey), buttonID);
        gridViewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, gridViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }






}
