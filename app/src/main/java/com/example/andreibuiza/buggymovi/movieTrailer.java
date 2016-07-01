package com.example.andreibuiza.buggymovi;

/**
 * Created by AndreiBuiza on 6/30/2016.
 */
public class movieTrailer {

    private String trailerKey;
    private String name;
    private String site;
    private String type;

    public movieTrailer(String trailerKey_, String name_, String site_, String type_){
        trailerKey = trailerKey_;
        name= name_;
        site= site_;
        type= type_;

    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "movieTrailer{" +
                "trailerKey='" + trailerKey + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
