package com.example.android.blends.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PlacesContract {

    public static final String PLACES_CONTENT_SCHEME = "content://";
    public static final String PLACES_CONTENT_AUTHORITY = "com.example.android.blends";
    public static final String PLACES_PATH = "cafe";
    public static final String PLACES_CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PLACES_CONTENT_AUTHORITY + "/" + PLACES_PATH;
    public static final String PLACES_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PLACES_CONTENT_AUTHORITY + "/" + PLACES_PATH;
    public static final String PLACES_PATH_REVIEWS = "reviews";
    public static final Uri PLACES_BASE_CONTENT_URI = Uri.parse(PLACES_CONTENT_SCHEME +
            PLACES_CONTENT_AUTHORITY);


    public static final class PlacesEntry implements BaseColumns {
        public static final Uri PLACES_CONTENT_URI = PLACES_BASE_CONTENT_URI.buildUpon()
                .appendEncodedPath(PLACES_PATH)
                .build();

        // places table
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_PLACE_ID = "id";
        public static final String COLUMN_PLACE_NAME = "name";
        public static final String COLUMN_PLACE_ADDRESS = "address";
        public static final String COLUMN_PLACE_NUMBER = "phoneNumber";
        public static final String COLUMN_PLACE_WEBSITE = "website";
        public static final String COLUMN_PLACE_RATING = "rating";
        public static final String COLUMN_PLACE_LAT = "lat";
        public static final String COLUMN_PLACE_LNG = "lng";
        public static final String COLUMN_PLACE_IMAGE = "placeImage";
        public static final String COLUMN_OPENING_HOURS = "openingHours";
        public static final String COLUMN_PRICE_LEVEL = "priceLevel";
        public static final String COLUMN_PLACE_WANT_TO_SEE = "wantToSee";
    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri REVIEWS_CONTENT_URI = PLACES_BASE_CONTENT_URI.buildUpon()
                .appendPath(PLACES_PATH_REVIEWS)
                .build();

        public static final String REVIEWS_TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_PLACE_ID = "placeId";
        public static final String COLUMN_REVIEW_AUTHOR = "author_name";
        public static final String COLUMN_REVIEW_TEXT = "text";
        public static final String COLUMN_REVIEW_RATING = "rating";
    }
}
