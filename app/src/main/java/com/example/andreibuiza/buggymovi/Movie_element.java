package com.example.andreibuiza.buggymovi;

/**
 * Created by AndreiBuiza on 6/15/2016.
 */

//TODO: To learn more, in order to save and recover the dynamic
// data using savedInstanceState, you can let this class implement Parcelable.
// By doing so, you can optimize your app to save dynamic data/state efficiently.
    // ^^^ from Udacity
public class Movie_element {
    private String posterURL;
    private String title;
    private String synopsis;
    private int rating;
    private String releaseDate;

    public Movie_element(){

    }

    public Movie_element(String poster, String title_, String synopsis_, int rating_, String releaseDate_){
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

    public int getRating() {
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
}
