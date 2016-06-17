package com.example.andreibuiza.buggymovi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AndreiBuiza on 6/13/2016.
 */
public class theMovieDB_API_response implements Parcelable{
    private String LOG_TAG=theMovieDB_API_response.class.getSimpleName();

    public static final String DATA_LOCK=theMovieDB_API_response.class.getSimpleName();

    private String Configuration_str;
    private String Popular_movies_str;
    private String Top_Rated_movies_str;

    public theMovieDB_API_response (String conf, String pop, String top){
        Configuration_str=conf;
        Popular_movies_str=pop;
        Top_Rated_movies_str=top;
        //call method to construct the image URLs
    }

    private theMovieDB_API_response(Parcel in){
        String [] values= new String[3];
        in.readStringArray(values);

        Configuration_str=values[0];
        Popular_movies_str=values[1];
        Top_Rated_movies_str=values[2];

    }

    //Extra static variable required by the Parcelable interface
    public static final Parcelable.Creator<theMovieDB_API_response> CREATOR
            = new Parcelable.Creator<theMovieDB_API_response>() {
        public theMovieDB_API_response createFromParcel(Parcel in) {
            return new theMovieDB_API_response(in);
        }

        public theMovieDB_API_response[] newArray(int size) {
            return new theMovieDB_API_response[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{Configuration_str, Popular_movies_str, Top_Rated_movies_str});
    }

    public String getConfiguration_str() {
        return Configuration_str;
    }

    public String getPopular_movies_str() {
        return Popular_movies_str;
    }

    public String getTop_Rated_movies_str() {
        return Top_Rated_movies_str;
    }
}
