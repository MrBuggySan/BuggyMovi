package com.example.andreibuiza.buggymovi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

/**
 * Created by AndreiBuiza on 6/11/2016.
 */
public class MovieGridFragment extends Fragment {
    private final String LOG_TAG= MovieGridFragment.class.getSimpleName();
    private Data_Extracts allMovieData;
    @Override
    public void onCreate (Bundle savedInstanceState){
        //I'm not sure why we have to call super.onCreate
        super.onCreate(savedInstanceState);
    }

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviegrid, container, false);

        theMovieDB_API_response API_full_response = (theMovieDB_API_response) getArguments().getParcelable(theMovieDB_API_response.DATA_LOCK);
        theMovieDB_JSON JSON_response;

        try{
            JSON_response= new theMovieDB_JSON(API_full_response.get_String_response());
            String img_base_url = JSON_response.getImgBaseURL();

            allMovieData = new Data_Extracts();


        }catch(JSONException e){
            Log.e(LOG_TAG, "Failed to create JSON objects out of API response");
        }







        return rootView;
    }


}
