package com.example.andreibuiza.buggymovi;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AndreiBuiza on 6/13/2016.
 */
public class theMovieDB_API_response implements Parcelable{
    private String LOG_TAG=theMovieDB_API_response.class.getSimpleName();

    String images_base_URL;

    public static final String DATA_LOCK=theMovieDB_API_response.class.getSimpleName();

    public JSONObject Configuration;
    public JSONObject Popular_movies;
    public JSONObject Top_Rated_movies;

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

    public String toString(){
        return Configuration.toString()+"\n"+Popular_movies.toString()+"n"+Top_Rated_movies.toString();
    }

    public void setup (){
        Log.d(LOG_TAG, "converting the string to JSON");
        try{
            Configuration=new JSONObject(Configuration_str);
            Popular_movies=new JSONObject(Popular_movies_str);
            Top_Rated_movies=new JSONObject(Top_Rated_movies_str);
        }catch(JSONException e){
            Log.e(LOG_TAG,"Unable to convert the strings into JSONObject" + e);
        }

        try{
            JSONObject images= Configuration.getJSONObject("images");
            images_base_URL = images.getString("base_url") + images.getJSONArray("poster_sizes").get(2);
        }catch(JSONException e){
            Log.e(LOG_TAG, "JSONObject access error...");
        }

        //call method to construct the image URLs
        createImgURLs_pop_movies();
        createImgURLs_top_movies();
    }

    public String[] get_String_response(){
        return new String[]{Configuration_str, Popular_movies_str, Top_Rated_movies_str};
    }

    //Method that constructs the image URLs for the popular movies
    private void createImgURLs_pop_movies(){

    }

    //Method that constructs the image URLs for the popular movies
    private void createImgURLs_top_movies(){

    }
}
