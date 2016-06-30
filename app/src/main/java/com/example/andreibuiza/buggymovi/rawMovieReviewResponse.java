package com.example.andreibuiza.buggymovi;

/**
 * Created by AndreiBuiza on 6/30/2016.
 */
public class rawMovieReviewResponse {

    private String LOG_TAG=rawMovieReviewResponse.class.getSimpleName();

    private String MovieTrailer_str;
    private String MovieReview_str;


    public rawMovieReviewResponse (String trailer, String reviews){
        MovieTrailer_str=trailer;
        MovieReview_str=reviews;
    }

    public String getMovieTrailer_str() {
        return MovieTrailer_str;
    }

    public String getMovieReview_str() {
        return MovieReview_str;
    }
}
