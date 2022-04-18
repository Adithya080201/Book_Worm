package com.example.android.bookworm;

public class Books {
    private String mBookName;
    private String mAuthor;
    private String mPublications;
    private double mRating;
    private double mPrice;
    private String mUrl;

    public Books(String bookName,String author, String publications, double rating, double price, String url) {
        mBookName = bookName;
        mPublications = publications;
        mAuthor = author;
        mRating = rating;
        mPrice = price;
        mUrl = url;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getAuthorName(){
        return mAuthor;
    }

    public String getPublicatiions() {
        return mPublications;
    }

    public double getRating() {
        return mRating;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getURL() {
        return mUrl;
    }

}
