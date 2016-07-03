package com.example.andreibuiza.buggymovi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

/**
 * Created by AndreiBuiza on 7/3/2016.
 */
public class onMovieTrailerClickListener implements View.OnClickListener {
    private String videoID;
    private Context mContext;
    private final String LOG_TAG = onMovieTrailerClickListener.class.getSimpleName();

    public onMovieTrailerClickListener(String id, Context context_){
        mContext = context_;
        videoID=id;
    }
    
    @Override
    public void onClick(View v){
        //Start playing the youtube video
        String youtubeLink=new String("http://www.youtube.com/watch?v=" + videoID);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink) );

        //Check if there are apps that will receive this intent
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }else{
            Log.d(LOG_TAG, "Couldn't call " + youtubeLink + ", no receiving apps installed!");
        }

    }
}
