package com.example.andreibuiza.buggymovi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */
public class theMovieDB_JSON {
    private String LOG_TAG = theMovieDB_JSON.class.getSimpleName();
    private JSONObject Configuration;
    private JSONObject Popular_movies;
    private JSONObject Top_Rated_movies;

    public theMovieDB_JSON(String[] str_response)throws JSONException{
        Configuration=new JSONObject(str_response[0]);
        Popular_movies=new JSONObject(str_response[1]);
        Top_Rated_movies=new JSONObject(str_response[2]);
    }

    /**
     * Return the string representation of the base URL for the posters
     * @return
     * @throws JSONException
     */
    public String getImgBaseURL()throws  JSONException{
        String images_base_URL;

            JSONObject images= Configuration.getJSONObject("images");

            //http://image.tmdb.org/t/p/w154/
            images_base_URL = images.getString("base_url") + '/' + images.getJSONArray("poster_sizes").get(2) + '/';
        return images_base_URL;
    }


}
