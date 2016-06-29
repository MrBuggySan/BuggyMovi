package com.example.andreibuiza.buggymovi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */

//TODO: To learn more, in order to save and recover the dynamic
// data using savedInstanceState, you can let this class implement Parcelable.
// By doing so, you can optimize your app to save dynamic data/state efficiently.
    // ^^^ from Udacity
public class Movie_element implements Parcelable{
    private String posterURL;
    private String fullPosterURL;
    private String title;
    private String synopsis;
    private double rating;
    private String releaseDate;

    public Movie_element(){

    }

    public String getFullPosterURL() {
        return fullPosterURL;
    }

    public void setFullPosterURL(String baseURL) {
        fullPosterURL = baseURL + posterURL;
    }

    public Movie_element(String poster, String title_, String synopsis_, double rating_, String releaseDate_){
        posterURL=poster;
        title=title_;
        synopsis=synopsis_;
        rating=rating_;
        releaseDate=releaseDate_;
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

    @Override
    public String toString() {
        return "Movie_element{" +
                "posterURL='" + posterURL + '\'' +
                ", title='" + title + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
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
