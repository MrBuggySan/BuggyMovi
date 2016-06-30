package com.example.andreibuiza.buggymovi;

/**
 * Created by AndreiBuiza on 6/30/2016.
 */
public class movieReview {
    private String author;
    private String content;

    public movieReview(String author_, String content_){
        author=author_;
        content=content_;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "movieReview{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
