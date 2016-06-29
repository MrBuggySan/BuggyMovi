package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by AndreiBuiza on 6/23/2016.
 */
public class DetailFragment extends Fragment{
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detailfragment, container, false);

        Bundle bundle = this.getArguments();

        Movie_element movieSelected = bundle.getParcelable(getString(R.string.movieSelectedkey));

        //TODO: add an image browser to browse through different images related to the movie
        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);

        //TODO: figure out a way to work with dp instead of raw px when drawing the images
        Picasso.with(getContext())
                .load(movieSelected.getFullPosterURL())
                .resize(780, (int)(780 * 1.5))
                .into(poster);

        TextView synopsis = (TextView) rootView.findViewById(R.id.sysnopsis);
        synopsis.setText(movieSelected.getSynopsis());

        TextView ratingtext = (TextView) rootView.findViewById(R.id.ratingText);
        ratingtext.setText(movieSelected.getRating() + " / 10");

        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        releaseDate.setText("Release Date: "+ movieSelected.getReleaseDate());



        //turn off the spinner from moviegridfragment
        ActionBar myToolbar = (ActionBar)((AppCompatActivity) getActivity()).getSupportActionBar();
        myToolbar.setDisplayShowCustomEnabled(false);
        myToolbar.setDisplayShowTitleEnabled(true);
        myToolbar.setTitle(movieSelected.getTitle());


        return rootView;
    }

}
