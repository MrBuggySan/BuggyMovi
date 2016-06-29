package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
        String baseImgURL = bundle.getString(getString(R.string.baseImgURLkey));
        String  posterURL = bundle.getString(getString(R.string.posterURLkey));
        String  title = bundle.getString(getString(R.string.titlekey));
        String  synopsistext = bundle.getString(getString(R.string.synopsiskey));
        //TODO: display 1 decimal on this number
        //TODO: remove the stars (it's too gimmicky, the number should be enough to show the rating)
        int  ratingValue = bundle.getInt(getString(R.string.ratingkey));
        String  releaseDateText = bundle.getString(getString(R.string.releaseDatekey));

//        Log.d(LOG_TAG, "img URL to be used: " + baseImgURL + posterURL);
//        Log.d(LOG_TAG, "Title of the movie: " + title);



        //TODO: add an image browser to browse through different images related to the movie
        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);
//        Picasso.with(getContext())
//                .load(baseImgURL + posterURL)
//                .resize(poster.getWidth(), (int) (poster.getWidth() * 1.5))
//                .into(poster);
        //TODO: figure out a way to work with dp instead of raw px when drawing the images
        Picasso.with(getContext())
                .load(baseImgURL + posterURL)
                .resize(780, (int)(780 * 1.5))
                .into(poster);




        TextView movieTitle = (TextView) rootView.findViewById(R.id.movieTitle);
        movieTitle.setText(title);

        TextView synopsis = (TextView) rootView.findViewById(R.id.sysnopsis);
        synopsis.setText(synopsistext);

        RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBar);
        rating.setRating(ratingValue);

        TextView ratingtext = (TextView) rootView.findViewById(R.id.ratingText);
        ratingtext.setText(ratingValue+"/10");

        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        releaseDate.setText("Release Date: "+ releaseDateText);



        //turn off the spinner from moviegridfragment
        ActionBar myToolbar = (ActionBar)((AppCompatActivity) getActivity()).getSupportActionBar();
        myToolbar.setDisplayShowCustomEnabled(false);
        myToolbar.setDisplayShowTitleEnabled(true);
        myToolbar.setTitle(title);





        return rootView;
    }

}
