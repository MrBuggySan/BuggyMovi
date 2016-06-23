package com.example.andreibuiza.buggymovi;

/**
 * Created by AndreiBuiza on 6/13/2016.
 */
public class theMovieDB_API_response {
    private String LOG_TAG=theMovieDB_API_response.class.getSimpleName();

    //public static final String DATA_LOCK=theMovieDB_API_response.class.getSimpleName();

    private String Configuration_str;
    private String Category_str;


    public theMovieDB_API_response (String conf, String category){
        Configuration_str=conf;
        Category_str=category;
    }

    public String getConfiguration_str() {
        return Configuration_str;
    }

    public String getCategory_str() {
        return Category_str;
    }
}
