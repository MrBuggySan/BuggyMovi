package com.example.andreibuiza.buggymovi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */
public class theMovieDB_JSON {
    private String LOG_TAG = theMovieDB_JSON.class.getSimpleName();
    private JSONObject Configuration;
    private JSONObject Category;
    private String baseImgURL;

    public theMovieDB_JSON(String conf, String cat)throws JSONException{
        Configuration=new JSONObject(conf);
        Category=new JSONObject(cat);
    }

    public int getCategory_size()throws JSONException{
        return Category.getJSONArray("results").length();
    }

    /**
     * Set all of the data required for each movie
     */
    public void setAll_of(Data_Extracts movieDatabase ) throws JSONException{
        Movie_element[] cat = movieDatabase.getCatMovies();
        baseImgURL=getImgBaseURL();

        //The Data_Extracts object will deal with COMPLETING the img URL
        movieDatabase.setBaseImgURL(baseImgURL);

        //Extract the data
        for(int i = 0 ; i < cat.length; i++){
            cat[i]= new Movie_element(getCatImageURL_of(i),getCatMovieTitle(i), getCatSynopsis(i), getCatRating(i), getCatReleaseDate(i) );
        }


    }

    /**
     * Return the string representation of the base URL for the posters
     * @return
     * @throws JSONException
     */
    private String getImgBaseURL()throws  JSONException{
        String images_base_URL;

            JSONObject images= Configuration.getJSONObject("images");

            //http://image.tmdb.org/t/p/w154/
            //I will let Data_Extracts decide on the appropriate width size
            images_base_URL = images.getString("base_url") + '/';
        return images_base_URL;
    }

    /**
     *Get the popular movie poster
     */
    private String getCatImageURL_of(int i) throws JSONException{
        //return baseImgURL + Popular_movies.getJSONArray("results").getJSONObject(i).getString("poster_path");
        //Instead of including the baseImgURL I will let Data_Extracts create it later on
        return Category.getJSONArray("results").getJSONObject(i).getString("poster_path");
    }

    /**
     * Get the movie title of popular movie
     */
    private String getCatMovieTitle(int i) throws JSONException{
        return Category.getJSONArray("results").getJSONObject(i).getString("original_title");
    }

    /**
     * Get the sysnopsis of popular movie
     */
    private String getCatSynopsis(int i)throws JSONException{
        return Category.getJSONArray("results").getJSONObject(i).getString("overview");
    }


    /**
     * Get all the ratings of the popular movies
     */
    private double getCatRating(int i) throws JSONException {
        return Category.getJSONArray("results").getJSONObject(i).getDouble("vote_average");
    }

      /**
     * Get release date of the movie
     */
     private String getCatReleaseDate (int i) throws JSONException {
        return Category.getJSONArray("results").getJSONObject(i).getString("release_date");
    }




}
