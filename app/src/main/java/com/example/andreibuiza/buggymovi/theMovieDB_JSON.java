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
    private String baseImgURL;

    public theMovieDB_JSON(String conf, String pop, String top)throws JSONException{
        Configuration=new JSONObject(conf);
        Popular_movies=new JSONObject(pop);
        Top_Rated_movies=new JSONObject(top);
    }

    public int getPopularMovie_size()throws JSONException{
        return Popular_movies.getJSONArray("results").length();
    }

    public int getTopRatedMovie_size()throws JSONException{
        return Top_Rated_movies.getJSONArray("results").length();
    }

    /**
     * Set all of the data required for each movie
     */
    public void setAll_of(Data_Extracts movieDatabase ) throws JSONException{
        Movie_element[] pop = movieDatabase.getPopularMovies();
        Movie_element[] top = movieDatabase.getTopRatedMovies();
        baseImgURL=getImgBaseURL();
        movieDatabase.setBaseImgURL(baseImgURL);
        //Popular movies
        for(int i = 0 ; i < pop.length; i++){
            pop[i]= new Movie_element(getPopImageURL_of(i),getPopMovieTitle(i), getPopSynopsis(i), getPopRating(i), getPopReleaseDate(i) );
        }

        // Top rated movies
        for(int i = 0 ; i < top.length; i++){
            top[i]= new Movie_element(getTopImageURL_of(i), getTopMovieTitle(i), getTopSynopsis(i), getTopRating(i), getTopReleaseDate(i));
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



            //images_base_URL = images.getString("base_url") + '/' + images.getJSONArray("poster_sizes").get(2) + '/';

            //I will let Data_Extracts decide on the appropriate width size
            images_base_URL = images.getString("base_url") + '/';
        return images_base_URL;
    }

    /**
     *Get the popular movie poster
     */
    private String getPopImageURL_of(int i) throws JSONException{
        //return baseImgURL + Popular_movies.getJSONArray("results").getJSONObject(i).getString("poster_path");
        //Instead of including the baseImgURL I will let Data_Extracts create it later on
        return Popular_movies.getJSONArray("results").getJSONObject(i).getString("poster_path");
    }

    /**
     *Get the top rated movie poster
     */
    private String getTopImageURL_of(int i) throws JSONException{
        //return baseImgURL + Top_Rated_movies.getJSONArray("results").getJSONObject(i).getString("poster_path");
        //Instead of including the baseImgURL I will let Data_Extracts create it later on
        return Top_Rated_movies.getJSONArray("results").getJSONObject(i).getString("poster_path");

    }

    /**
     * Get the movie title of popular movie
     */
    private String getPopMovieTitle(int i) throws JSONException{
        return Popular_movies.getJSONArray("results").getJSONObject(i).getString("original_title");
    }

    /**
     * Get the movie title of top rated movie
     */
    private String getTopMovieTitle(int i) throws JSONException{
        return Top_Rated_movies.getJSONArray("results").getJSONObject(i).getString("original_title");
    }

    /**
     * Get the sysnopsis of popular movie
     */
    private String getPopSynopsis(int i)throws JSONException{
        return Popular_movies.getJSONArray("results").getJSONObject(i).getString("overview");
    }

    /**
     * Get the sysnopsis of top rated movie
     */
    private String getTopSynopsis(int i)throws JSONException{
        return Top_Rated_movies.getJSONArray("results").getJSONObject(i).getString("overview");
    }


    /**
     * Get all the ratings of the popular movies
     */
    private int getPopRating(int i) throws JSONException {
        return Popular_movies.getJSONArray("results").getJSONObject(i).getInt("vote_average");
    }

    /**
     * Get all the ratings of the top rated movies
     */
    private int getTopRating(int i) throws JSONException {
        return Top_Rated_movies.getJSONArray("results").getJSONObject(i).getInt("vote_average");
    }


    /**
     * Get release date of popular movies
     */
     private String getPopReleaseDate (int i) throws JSONException {
        return Popular_movies.getJSONArray("results").getJSONObject(i).getString("release_date");
    }

    /**
     * Get release date of popular movies
     */
    private String getTopReleaseDate (int i) throws JSONException {
        return Top_Rated_movies.getJSONArray("results").getJSONObject(i).getString("release_date");
    }


}
