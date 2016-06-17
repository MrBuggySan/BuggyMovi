package com.example.andreibuiza.buggymovi;

import android.util.Log;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */
public class Data_Extracts {
    private final String LOG_TAG = Data_Extracts.class.getSimpleName();
    //public so I can easily manipulate the data of each element in the array.
    private Movie_element[] popularMovies;
    private Movie_element[] topRatedMovies;



    /**
     * Notes: This constructor only makes an array, it does no initialization
     * @param topRated_size
     * @param popular_size
     */
    public Data_Extracts(int topRated_size, int popular_size){
        topRatedMovies = new Movie_element[topRated_size];
//        for(int i = 0 ; i < topRatedMovies.length ; i++){
//            topRatedMovies[i] = new Movie_element();
//        }

        popularMovies = new Movie_element[popular_size];
//        for(int i = 0 ; i < popularMovies.length ; i++){
//            popularMovies[i] = new Movie_element();
//        }
    }

    public Movie_element[] getTopRatedMovies() {
        return topRatedMovies;
    }

    public Movie_element[] getPopularMovies() {
        return popularMovies;
    }

    public void setTopRatedMovies(Movie_element[] topRatedMovies) {
        this.topRatedMovies = topRatedMovies;
    }

    public void setPopularMovies(Movie_element[] popularMovies) {
        this.popularMovies = popularMovies;
    }

    public void testContent(){
        Log.d(LOG_TAG,"\n\n\n ==============Popular Movies=============== \n\n\n");
        for( int i = 0 ; i < popularMovies.length ; i++){
            Log.d(LOG_TAG, popularMovies[i].toString());
        }
        Log.d(LOG_TAG,"\n\n\n ==============Top Rated Movies=============== \n\n\n");
        for( int i = 0 ; i < topRatedMovies.length ; i++){
            Log.d(LOG_TAG, topRatedMovies[i].toString());
        }
    }
}
