package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        View rootView = inflater.inflate(R.layout.moviegrid, container, false);

        Bundle bundle = this.getArguments();
        String baseImgURL = bundle.getString(getString(R.string.baseImgURLkey));
        String  posterURL = bundle.getString(getString(R.string.posterURLkey));
        String  title = bundle.getString(getString(R.string.titlekey));
        String  synopsis = bundle.getString(getString(R.string.synopsiskey));
        int  rating = bundle.getInt(getString(R.string.ratingkey));
        String  releaseDate = bundle.getString(getString(R.string.releaseDatekey));

        Log.d(LOG_TAG, "img URL to be used: " + baseImgURL + posterURL);
        Log.d(LOG_TAG, "Title of the movie: " + title);


        //TODO: Display the information on the layout

        ImageView poster = (ImageView) rootView.findViewById(R.id.posterImageView);
        Picasso.with(getContext())
                .load(baseImgURL + posterURL)
                .resize(poster.getWidth(), (int) (poster.getWidth() * 1.5))
                .into(poster);




        //TODO: apply a back button on the toolbar
        //TODO: Change the text of the toolbar to be the title of the selected movie


        return rootView;
    }

}
