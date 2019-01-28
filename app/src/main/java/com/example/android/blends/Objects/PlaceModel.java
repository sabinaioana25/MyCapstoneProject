package com.example.android.blends.Objects;

import java.util.List;

public class PlaceModel {

    private String placeId;
    private String name;
    private String address;
    private String phoneNumber;
    private String websiteUri;
    private float rating;
    private String lat;
    private String lng;
    private String image;
    private String openNow;
    private String priceLevel;
    private List<ReviewModel> reviews;
    private int wantToSee;

    public PlaceModel(String placeId,
                      String name,
                      String address,
                      String phoneNumber,
                      String websiteUri,
                      float rating,
                      String lat,
                      String lng,
                      String image,
                      String openNow,
                      String priceLevel,
                      List<ReviewModel> reviews,
                      int wantToSee) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.openNow = openNow;
        this.priceLevel = priceLevel;
        this.reviews = reviews;
        this.wantToSee = wantToSee;
    }

    public PlaceModel() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(String websiteUri) {
        this.websiteUri = websiteUri;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public List<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    public int getWantToSee() {
        return wantToSee;
    }

    public void setWantToSee(int wantToSee) {
        this.wantToSee = wantToSee;
    }
}
