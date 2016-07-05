package com.example.andreibuiza.buggymovi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by AndreiBuiza on 7/5/2016.
 */
public class DetailActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailactivity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        // Set the tool bar back button
        //TODO: the home button is buggy, it does not display the previous category
        //it goes back to very start which is popular movies
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra(DetailActivity.class.getSimpleName());


        //Use a bundle to set the arguments of the detail fragment
        if(savedInstanceState == null){
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.detail_container,detailFragment).commit();
        }
    }


}
