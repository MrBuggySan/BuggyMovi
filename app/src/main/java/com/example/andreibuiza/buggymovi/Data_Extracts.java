package com.example.andreibuiza.buggymovi;

import android.util.Log;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */
public class Data_Extracts {
    private final String LOG_TAG = Data_Extracts.class.getSimpleName();
    //public so I can easily manipulate the data of each element in the array.
    private Movie_element[] catMovies;
    private String baseImgURL;



    /**
     * Notes: This constructor only makes an array, it does no initialization
     */
    public Data_Extracts(int categoty_size){
        catMovies = new Movie_element[categoty_size];

    }

    public void setCatMovies(Movie_element[] catMovies) {
        this.catMovies = catMovies;
    }

    public Movie_element[] getCatMovies() {
        return catMovies;
    }





    public String getBaseImgURL(int elementWidth) {
        String imgWidth = "w500";
        if(  elementWidth <= 92 )                           imgWidth = "w92";
        if(  92 < elementWidth && elementWidth <= 154 )     imgWidth = "w154";
        if( 154 < elementWidth && elementWidth <= 185)      imgWidth = "w185";
        if( 185 < elementWidth && elementWidth <= 342)      imgWidth = "w342";
        if( 342 < elementWidth && elementWidth <= 500)      imgWidth = "w500";
        if( 500 < elementWidth && elementWidth <= 780)      imgWidth = "w780";
        //There is also an 'original' size, I will support that later.
        return baseImgURL + imgWidth + '/';
    }

    public void setBaseImgURL(String baseImgURL) {
        this.baseImgURL = baseImgURL;
    }

    public void testContent(){

        for( int i = 0 ; i < catMovies.length ; i++){
            Log.d(LOG_TAG, catMovies[i].toString());
        }

    }
}
