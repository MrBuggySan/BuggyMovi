package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FrontMenuFragment.OnButtonSelectedListener,
        MovieGridFragment.OnPosterSelectedListener{
    //TODO: Make sure that the user's internet is connected
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private theMovieDB_API_response API_full_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //myToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setDisplayShowCustomEnabled(false);
        //Log.d(LOG_TAG,"we have entered onCreate");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new FrontMenuFragment()).commit();
        }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: The detail fragment will be activated when 'latest movie' is selected by the user
    public void OnButtonSelected(int buttonID){
        MovieGridFragment gridViewFragment = new MovieGridFragment();

        //set the argument of the MovieGridFragment
        Bundle args = new Bundle();
        args.putInt(getString(R.string.menuToGridKey), buttonID);
        gridViewFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, gridViewFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void OnPosterSelected(Movie_element movieSelected, String baseImgURL){
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();


        args.putString(getString(R.string.baseImgURLkey), baseImgURL);
        args.putString(getString(R.string.posterURLkey), movieSelected.getPosterURL());
        args.putString(getString(R.string.titlekey), movieSelected.getTitle());
        args.putString(getString(R.string.synopsiskey), movieSelected.getSynopsis());
        args.putInt(getString(R.string.ratingkey), movieSelected.getRating());
        args.putString(getString(R.string.releaseDatekey), movieSelected.getReleaseDate());

        detailFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    //TODO: Switch the fragments when the user selects a new category, the fragment to be replaced will not be added to the back stack
    //public void



}
