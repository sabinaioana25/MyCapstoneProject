package com.example.android.blends.Utilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.Objects.ReviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.blends.Utilities.JsonUtilsConstants.GEOMETRY;
import static com.example.android.blends.Utilities.JsonUtilsConstants.LATITUDE;
import static com.example.android.blends.Utilities.JsonUtilsConstants.LOCATION;
import static com.example.android.blends.Utilities.JsonUtilsConstants.LONGITUDE;
import static com.example.android.blends.Utilities.JsonUtilsConstants.NAME;
import static com.example.android.blends.Utilities.JsonUtilsConstants.OPENING_HOURS;
import static com.example.android.blends.Utilities.JsonUtilsConstants.OPEN_NOW;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PHOTOS;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PHOTO_REFERENCE;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_FORMATTED_ADDRESS;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_ID;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_INT_PHONE;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_OPEN_WEEKDAY;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_RESULT;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PLACE_REVIEWS;
import static com.example.android.blends.Utilities.JsonUtilsConstants.PRICE_LEVEL;
import static com.example.android.blends.Utilities.JsonUtilsConstants.RATING;
import static com.example.android.blends.Utilities.JsonUtilsConstants.RESULTS;
import static com.example.android.blends.Utilities.JsonUtilsConstants.REVIEW_AUTHOR_NAME;
import static com.example.android.blends.Utilities.JsonUtilsConstants.REVIEW_TEXT;
import static com.example.android.blends.Utilities.JsonUtilsConstants.VICINITY;
import static com.example.android.blends.Utilities.JsonUtilsConstants.WEBSITE;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    public JSONUtils() {
        // mandatory empty constructor
    }

    public static List<PlaceModel> extractFeaturesFromJson(String jsonResponse) {

        /*
        Create an empty List<PlaceModel>
         */
        List<PlaceModel> placeModelList = new ArrayList<>();
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        /*
         Extract JSON data for the list of places
        */
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray jsonResults = baseJsonResponse.getJSONArray(RESULTS);

            String placeId;
            String name;
            String vicinity;
            String open_now;
            String image_reference = null;
            String price_level = null;
            float rating = 0;
            String lat;
            String lng;

            for (int i = 0; i < jsonResults.length(); i++) {

                JSONObject placeJsonObject = jsonResults.getJSONObject(i);
                JSONObject geometryObject = placeJsonObject.getJSONObject(GEOMETRY);
                JSONObject locationObject = geometryObject.getJSONObject(LOCATION);
                lat = locationObject.getString(LATITUDE);
                lng = locationObject.optString(LONGITUDE);
                name = placeJsonObject.optString(NAME);
                JSONObject placeOpeningHours = placeJsonObject.optJSONObject(OPENING_HOURS);
                open_now = placeOpeningHours.optString(OPEN_NOW);

                if (open_now.equals("false")) {
                    open_now = "Closed";
                } else {
                    open_now = "Open";
                }
                JSONArray placePhotos = placeJsonObject.optJSONArray(PHOTOS);
                for (int j = 0; j < placePhotos.length(); j++) {
                    JSONObject placePhotoResponse = placePhotos.optJSONObject(j);
                    image_reference = placePhotoResponse.optString(PHOTO_REFERENCE);
                }
                placeId = placeJsonObject.optString(PLACE_ID);
                if (price_level != null) {
                    price_level = placeJsonObject.optString(PRICE_LEVEL);
                } else {
                    Log.v(TAG, "extractFeaturesFromJson: price_level not available");
                }
                vicinity = placeJsonObject.optString(VICINITY);

                PlaceModel placeModelItem = new PlaceModel(
                        placeId,
                        name,
                        vicinity,
                        null,
                        null,
                        rating,
                        lat,
                        lng,
                        image_reference,
                        open_now,
                        price_level,
                        null,
                        0
                );
                placeModelList.add(placeModelItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeModelList;
    }

    public static PlaceModel extractPlaceDetailsFromJson(String jsonResponse) {

        List<ReviewModel> reviewModelPlace = new ArrayList<>();
        PlaceModel placeModel;

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            String name;
            String website;
            String formatted_address;
            String international_phone_number;
            String place_id;
            String lat;
            String lng;
            String review_author_name = null;
            String review_text = null;
            String reviews_rating = null;
            float rating;
            JSONObject jsonDetailResponse = new JSONObject(jsonResponse);
            JSONObject jsonDetailResult = jsonDetailResponse.getJSONObject(PLACE_RESULT);
            formatted_address = jsonDetailResult.optString(PLACE_FORMATTED_ADDRESS);
            international_phone_number = jsonDetailResult.optString(PLACE_INT_PHONE);
            name = jsonDetailResult.optString(NAME);
            JSONObject detailOpeningHours = jsonDetailResult.optJSONObject(OPENING_HOURS);
            JSONArray detailWeekOpen = detailOpeningHours.optJSONArray(PLACE_OPEN_WEEKDAY);
            for (int i = 0; i < detailWeekOpen.length(); i++) {
                String schedulePerDay = detailWeekOpen.optString(i);
                List<String> list;
                list = new ArrayList<>();
                list.add(0, schedulePerDay);
            }
            place_id = jsonDetailResult.optString(PLACE_ID);
            JSONObject geometryObject = jsonDetailResult.getJSONObject(GEOMETRY);
            JSONObject locationObject = geometryObject.getJSONObject(LOCATION);
            lat = locationObject.optString(LATITUDE);
            lng = locationObject.optString(LONGITUDE);


            rating = jsonDetailResult.optInt(RATING);
            website = jsonDetailResult.optString(WEBSITE);

            // extract json for reviews
            JSONArray singlePlaceReviews = jsonDetailResult.getJSONArray(PLACE_REVIEWS);
            if (singlePlaceReviews.length() > 0) {
                for (int j = 0; j < singlePlaceReviews.length(); j++) {
                    JSONObject singleReview = singlePlaceReviews.getJSONObject(j);
                    review_author_name = singleReview.optString(REVIEW_AUTHOR_NAME);
                    review_text = singleReview.optString(REVIEW_TEXT);
                    reviews_rating = singleReview.optString(RATING);
                    reviewModelPlace.add(new ReviewModel(review_author_name,
                            review_text, reviews_rating));
                }

                Log.i(TAG, "extractPlaceDetailsFromJson: " );
            }
            ReviewModel reviewModelDetail = new ReviewModel(
                    review_author_name,
                    review_text,
                    reviews_rating);
            reviewModelPlace.add(reviewModelDetail);

            placeModel = new PlaceModel(place_id,
                    name,
                    formatted_address,
                    international_phone_number,
                    website,
                    rating,
                    lat,
                    lng,
                    null,
                    detailWeekOpen.toString(),
                    null,
                    reviewModelPlace,
                    0
            );
            return placeModel;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}