package com.example.android.blends.Objects;

public class ReviewModel {
    private String reviewAuthorName;
    private String reviewText;
    private String reviewRating;

    public ReviewModel(String reviewAuthorName, String reviewText, String reviewRating) {
        this.reviewAuthorName = reviewAuthorName;
        this.reviewText = reviewText;
        this.reviewRating = reviewRating;
    }

    public String getReviewAuthorName() {
        return reviewAuthorName;
    }

    public void setReviewAuthorName(String reviewAuthorName) {
        this.reviewAuthorName = reviewAuthorName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }
}
