package com.example.android.blends.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.blends.Loaders.DetailPlacesLoader;
import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.Objects.ReviewModel;
import com.example.android.blends.R;
import com.example.android.blends.Utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.blends.Data.PlacesContract.PlacesEntry;
import static com.example.android.blends.Data.PlacesContract.ReviewsEntry;

public class DetailActivity extends
        AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final int ID_LOADER_DETAIL_PLACES = 1;
    public static final int ID_CURSOR_DETAIL_PLACES = 2;
    public static final int ID_REVIEW_LOADER = 3;
    public static final int ID_CURSOR_REVIEW_LOADER = 4;

    // place detail components
    private String placeId;
    public String placeName;
    private String placeFormattedAddress;
    private String placePhoneNumber;
    private String placeWebsite;
    private String placeSchedule;
    private String placeOverallRating;
    private List<ReviewModel> reviewsEntryList = null;
    private String placePriceLevel;
    private int wantToSeeInd = 0;
    private String lat;
    private String lng;
    private String reviewAuthor;
    private String reviewContent;
    public PlaceModel mObject = new PlaceModel();
    Button buttonFav;
    private boolean mIsEnabled;
    boolean buttonIsSelected = false;
    private MapActivity geofencing;


    private final String[] detailPlaceProjection = new String[]{
            PlacesEntry.COLUMN_PLACE_ID,
            PlacesEntry.COLUMN_PLACE_NAME,
            PlacesEntry.COLUMN_PLACE_ADDRESS,
            PlacesEntry.COLUMN_PLACE_NUMBER,
            PlacesEntry.COLUMN_PLACE_WEBSITE,
            PlacesEntry.COLUMN_PLACE_RATING,
            PlacesEntry.COLUMN_PLACE_LAT,
            PlacesEntry.COLUMN_PLACE_LNG,
            PlacesEntry.COLUMN_PLACE_IMAGE,
            PlacesEntry.COLUMN_OPENING_HOURS,
            PlacesEntry.COLUMN_PRICE_LEVEL,
            PlacesEntry.COLUMN_PLACE_WANT_TO_SEE
    };

    private final String[] reviewsProjection = new String[]{
            ReviewsEntry.COLUMN_REVIEW_PLACE_ID,
            ReviewsEntry.COLUMN_REVIEW_AUTHOR,
            ReviewsEntry.COLUMN_REVIEW_TEXT,
            ReviewsEntry.COLUMN_REVIEW_RATING
    };
    Switch enalbeGeofence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        Intent getPlaceDetails = getIntent();
        Bundle extras = getPlaceDetails.getExtras();
        if (extras != null) {
            placeId = extras.getString("place id");
        }
        NetworkUtils.buildDetailPlaceUrl(placeId);
        getSupportLoaderManager().initLoader(ID_CURSOR_DETAIL_PLACES, null, this);
        getSupportLoaderManager().initLoader(ID_CURSOR_REVIEW_LOADER, null, this);

        mIsEnabled = getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.setting_enabled),
                false);

        enalbeGeofence = findViewById(R.id.enable_switch);
        buttonFav = findViewById(R.id.icon_favourite);
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!buttonIsSelected) {
                    buttonFav.setSelected(true);
                    buttonIsSelected = true;
                    Log.e(TAG, "onClick: " + mObject);
                    addToDatabaseTable(ID_CURSOR_DETAIL_PLACES, mObject);
                    addToDatabaseTable(ID_CURSOR_REVIEW_LOADER, mObject);

                } else {
                    buttonFav.setSelected(false);
                    buttonIsSelected = false;
                    deleteInfoFromTable();
                }
            }
        });
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_CURSOR_DETAIL_PLACES:
                String placeSel = PlacesEntry.COLUMN_PLACE_ID + "=?";
                String[] placeSelArgs = new String[]{String.valueOf(placeId)};
                return new CursorLoader(getApplicationContext(),
                        PlacesEntry.PLACES_CONTENT_URI,
                        detailPlaceProjection,
                        placeSel,
                        placeSelArgs,
                        null);
            case ID_CURSOR_REVIEW_LOADER:
                String reviewSel = ReviewsEntry.COLUMN_REVIEW_PLACE_ID + "=?";
                String[] reviewSelArgs = new String[]{String.valueOf(placeId)};
                return new CursorLoader(this,
                        ReviewsEntry.REVIEWS_CONTENT_URI,
                        reviewsProjection,
                        reviewSel,
                        reviewSelArgs,
                        null);

            case ID_LOADER_DETAIL_PLACES:
                return new DetailPlacesLoader(this, placeId, ID_LOADER_DETAIL_PLACES);
            case ID_REVIEW_LOADER:
                return new DetailPlacesLoader(this, placeId, ID_REVIEW_LOADER);
            default:
                return new DetailPlacesLoader(this, placeId, ID_LOADER_DETAIL_PLACES);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object object) {
        switch (loader.getId()) {
            case ID_CURSOR_DETAIL_PLACES:
                Cursor placeCursor = (Cursor) object;
                if (placeCursor.getCount() > 0) {
                    placeCursor.moveToFirst();
                    populatePlaceValues(loader.getContext(), placeCursor,
                            ID_CURSOR_DETAIL_PLACES);
                    getSupportLoaderManager().initLoader(ID_CURSOR_REVIEW_LOADER, null, this);
                } else {
                    getSupportLoaderManager().restartLoader(ID_LOADER_DETAIL_PLACES, null, this);
                    getSupportLoaderManager().restartLoader(ID_REVIEW_LOADER, null, this);
                }
                break;
            case ID_CURSOR_REVIEW_LOADER:
                Cursor reviewCursor = (Cursor) object;
                populateReviewValues(reviewCursor, ID_CURSOR_REVIEW_LOADER);
                break;
            case ID_LOADER_DETAIL_PLACES:
                populatePlaceValues(loader.getContext(), object, ID_LOADER_DETAIL_PLACES);
                break;
            case ID_REVIEW_LOADER:
                populateReviewValues(loader.getContext(), ID_REVIEW_LOADER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // do nothing
    }

    public void populatePlaceValues(Context context, final Object object, int type) {
        switch (type) {
            case ID_CURSOR_DETAIL_PLACES:
                Cursor placeCursor = (Cursor) object;

                placeId = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_ID));
                placeName = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_NAME));
                placeFormattedAddress = placeCursor.getString(placeCursor.getColumnIndex
                        (PlacesEntry.COLUMN_PLACE_ADDRESS));
                placePhoneNumber = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_NUMBER));
                placeWebsite = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_WEBSITE));
                placeOverallRating = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_RATING));
                placeSchedule = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_OPENING_HOURS));
                placePriceLevel = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PRICE_LEVEL));
                wantToSeeInd = placeCursor.getInt(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_WANT_TO_SEE));
                lat = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_LAT));
                lng = placeCursor.getString(placeCursor.getColumnIndex(PlacesEntry
                        .COLUMN_PLACE_LNG));
                break;
            case ID_LOADER_DETAIL_PLACES:
                mObject = (PlaceModel) object;
                placeName = mObject.getName();
                placeFormattedAddress = mObject.getAddress();
                placePhoneNumber = mObject.getPhoneNumber();
                placeWebsite = mObject.getWebsiteUri();
                placeOverallRating = String.valueOf(mObject.getRating());
                placeSchedule = mObject.getOpenNow();
                placePriceLevel = mObject.getPriceLevel();
                wantToSeeInd = mObject.getWantToSee();
                lat = mObject.getLat();
                lng = mObject.getLng();
                break;
        }
        // place address
        TextView placeFormAddressTextView = findViewById(R.id.text_view_cafe_address);
        placeFormAddressTextView.setText(placeFormattedAddress);

        // place open schedule
        TextView weekDaysTextView = findViewById(R.id.text_view_days);
        weekDaysTextView.setText(placeSchedule);

        // place contact number
        TextView phoneNumberTextView = findViewById(R.id.text_view_phone_number);
        phoneNumberTextView.setText(placePhoneNumber);

        // place rating
        TextView tvPlaceOverallRating = findViewById(R.id.textViewOverallRatingValue);
        tvPlaceOverallRating.setText(placeOverallRating);
    }

    private void populateReviewValues(final Object object, int type) {
        reviewsEntryList = new ArrayList<>();
        switch (type) {
            case ID_CURSOR_REVIEW_LOADER:

                Cursor reviewCursor = (Cursor) object;
                for (int i = 0; i < reviewCursor.getCount(); i++) {
                    reviewCursor.moveToPosition(0);
                    reviewAuthor = reviewCursor.getString(reviewCursor.getColumnIndex
                            (ReviewsEntry.COLUMN_REVIEW_AUTHOR));
                    reviewContent = reviewCursor.getString(reviewCursor.getColumnIndex
                            (ReviewsEntry.COLUMN_REVIEW_TEXT));
                    reviewsEntryList.add(new ReviewModel(reviewAuthor, reviewContent, null));
                }
                break;
            case ID_LOADER_DETAIL_PLACES:
                mObject = (PlaceModel) object;
                reviewsEntryList = mObject.getReviews();
                break;
        }
        TextView reviewAuthorTextView = findViewById(R.id.textViewReviewAuthor);
        TextView reviewContentTextView = findViewById(R.id.textViewReviewContent);

        reviewAuthorTextView.setText(reviewAuthor + ":");
        reviewContentTextView.setText(reviewContent);

    }

    public void addToDatabaseTable(int loaderType, Object object) {
        ContentValues contentValues = new ContentValues();
        mObject = (PlaceModel) object;

        placeName = mObject.getName();
        placeFormattedAddress = mObject.getAddress();
        placePhoneNumber = mObject.getPhoneNumber();
        placeWebsite = mObject.getWebsiteUri();
        placeOverallRating = String.valueOf(mObject.getRating());
        placeSchedule = mObject.getOpenNow();
        placePriceLevel = mObject.getPriceLevel();
        reviewsEntryList = mObject.getReviews();
        lat = mObject.getLat();
        lng = mObject.getLng();

        switch (loaderType) {
            case ID_CURSOR_DETAIL_PLACES:
                contentValues.put(PlacesEntry.COLUMN_PLACE_ID, placeId);
                contentValues.put(PlacesEntry.COLUMN_PLACE_NAME, placeName);
                contentValues.put(PlacesEntry.COLUMN_PLACE_ADDRESS, placeFormattedAddress);
                contentValues.put(PlacesEntry.COLUMN_PLACE_NUMBER, placePhoneNumber);
                contentValues.put(PlacesEntry.COLUMN_PLACE_WEBSITE, placeWebsite);
                contentValues.put(PlacesEntry.COLUMN_PLACE_RATING, placeOverallRating);
                contentValues.put(PlacesEntry.COLUMN_PLACE_LAT, lat);
                contentValues.put(PlacesEntry.COLUMN_PLACE_LNG, lng);
                contentValues.put(PlacesEntry.COLUMN_PLACE_IMAGE, 0);
                contentValues.put(PlacesEntry.COLUMN_OPENING_HOURS, placeSchedule);
                contentValues.put(PlacesEntry.COLUMN_PRICE_LEVEL, placePriceLevel);
                contentValues.put(PlacesEntry.COLUMN_PLACE_WANT_TO_SEE, 0);
                getContentResolver().insert(PlacesEntry.PLACES_CONTENT_URI, contentValues);
                break;

            case ID_CURSOR_REVIEW_LOADER:
                String cursorReviewAuthor;
                String cursorReviewText;
                String cursorReviewRating;

                if (reviewsEntryList != null) {
                    for (int i = 0; i < reviewsEntryList.size(); i++) {
                        cursorReviewAuthor = reviewsEntryList.get(0).getReviewAuthorName();
                        cursorReviewText = reviewsEntryList.get(0).getReviewText();
                        cursorReviewRating = reviewsEntryList.get(0).getReviewRating();

                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_PLACE_ID, placeId);
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_AUTHOR, cursorReviewAuthor);
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_TEXT, cursorReviewText);
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_RATING, cursorReviewRating);
                        getContentResolver().insert(ReviewsEntry.REVIEWS_CONTENT_URI,
                                contentValues);
                    }
                }
                break;
        }
    }

    private void deleteInfoFromTable() {
        String selection = PlacesEntry.COLUMN_PLACE_ID + "=?";
        String[] selArgs = new String[]{String.valueOf(placeId)};
        String selReviews = ReviewsEntry.COLUMN_REVIEW_PLACE_ID + "=?";
        String[] selArgsReviews = new String[]{String.valueOf(placeId)};
        getContentResolver().delete(PlacesEntry.PLACES_CONTENT_URI, selection, selArgs);
        getContentResolver().delete(ReviewsEntry.REVIEWS_CONTENT_URI, selReviews, selArgsReviews);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_view_map) {
            Intent showPlaceOnMap = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + lat + ">," +
                    "<" + lng + ">?q=<" + lat + ">,<" + lng + ">(" + placeName + ")"));
            startActivity(showPlaceOnMap);
        } else if (id == R.id.action_settings) {
            //do something later
            Toast.makeText(this, "Hahaha", Toast.LENGTH_SHORT).show();

//            enalbeGeofence.setChecked(mIsEnabled);
//            enalbeGeofence.setOnCheckedChangeListener(new CompoundButton
// .OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//                    editor.putBoolean(getString(R.string.setting_enabled), isChecked);
//                    mIsEnabled = isChecked;
//                    editor.commit();
//                    if (isChecked) geofencing.registerAllGeofences();
//                    else geofencing.unRegisterAllGeofences();
//                }
//            });

        } else {
            //do another thing later
        }
        return super.onOptionsItemSelected(item);
    }
}