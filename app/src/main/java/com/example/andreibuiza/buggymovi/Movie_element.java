package com.example.andreibuiza.buggymovi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */

public class Movie_element implements Parcelable{
    private String posterURL;
    private String fullPosterURL;
    private String title;
    private String synopsis;
    private double rating;
    private String releaseDate;
    private String movieID;
    //assume all clips are going to be from YouTube, only work with 1 trailer per movie
    private String movieTrailerkey;
    //The size of the String array is determined by the results array size
    private movieReview[] movieReviews;



    public Movie_element(){

    }

    public String getFullPosterURL() {
        return fullPosterURL;
    }

    public void setFullPosterURL(String baseURL) {
        fullPosterURL = baseURL + posterURL;
    }

    public Movie_element(String poster, String title_, String synopsis_, double rating_, String releaseDate_,
                         int movieID_){
        posterURL=poster;
        title=title_;
        synopsis=synopsis_;
        rating=rating_;
        releaseDate=releaseDate_;
        movieID=Integer.toString(movieID_);
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieTrailerkey() {
        return movieTrailerkey;
    }

    public void setMovieTrailerkey(String movieTrailerkey) {
        this.movieTrailerkey = movieTrailerkey;
    }

    public movieReview[] getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(movieReview[] movieReviews) {
        this.movieReviews = movieReviews;
    }

    public boolean hasTrailer(){
        if(movieTrailerkey!=null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
       String first=  "Movie_element{" +
                "posterURL='" + posterURL + '\'' +
                ", fullPosterURL='" + fullPosterURL + '\'' +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                ", movieID='" + movieID + '\'' +
                ", movieTrailerkey='" + movieTrailerkey;
        String second= "";
        for(int i = 0 ; i < movieReviews.length ; i++){
            second+= " Review #: "+ i + " " +  movieReviews[i].toString();
        }

        return first + second ;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{posterURL, fullPosterURL, title, synopsis, releaseDate});
        out.writeDouble(rating);
    }

    private Movie_element(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        posterURL = data[0];
        fullPosterURL = data[1];
        title = data[2];
        synopsis = data[3];
        releaseDate = data[4];

        rating = in.readDouble();
    }

    public static final Parcelable.Creator<Movie_element> CREATOR
            = new Parcelable.Creator<Movie_element>() {
        public Movie_element createFromParcel(Parcel in) {
            return new Movie_element(in);
        }

        public Movie_element[] newArray(int size) {
            return new Movie_element[size];
        }
    };


}
