package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements FrontMenuFragment.OnButtonSelectedListener,
        MovieGridFragment.OnPosterSelectedListener{
    //TODO: Make sure that the user's internet is connected

    private theMovieDB_API_response API_full_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new FrontMenuFragment()).commit();
        }
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

        //TODO: set the arguments of the DetailFragment
        args.putString(getString(R.string.baseImgURLkey), baseImgURL);
        args.putString(getString(R.string.posterURLkey), movieSelected.getPosterURL());
        args.putString(getString(R.string.titlekey), movieSelected.getTitle());
        args.putString(getString(R.string.synopsiskey), movieSelected.getSynopsis());
        args.putInt(getString(R.string.ratingkey), movieSelected.getRating());
        args.putString(getString(R.string.releaseDatekey), movieSelected.getReleaseDate());

        detailFragment.setArguments(args);
        //TODO: launch the details fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    //TODO: Switch the fragments when the user selects a new category, the fragment to be replaced will not be added to the back stack
    //public void



}
