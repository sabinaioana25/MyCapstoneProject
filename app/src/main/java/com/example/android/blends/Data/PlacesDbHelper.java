package com.example.android.blends.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.blends.Data.PlacesContract.PlacesEntry;
import static com.example.android.blends.Data.PlacesContract.ReviewsEntry;

class PlacesDbHelper extends SQLiteOpenHelper {

    // name
    private static final String DATABASE_NAME = "cafes.db";
    private static final int DATABASE_VERSION = 1;


    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PLACES_TABLE =
                "CREATE TABLE " + PlacesEntry.TABLE_NAME + " (" +
                        PlacesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PlacesEntry.COLUMN_PLACE_ID + " INTEGER, " +
                        PlacesEntry.COLUMN_PLACE_NAME + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_ADDRESS + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_NUMBER + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_WEBSITE + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_RATING + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_LAT + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_LNG + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_IMAGE + " TEXT, " +
                        PlacesEntry.COLUMN_OPENING_HOURS + " TEXT, " +
                        PlacesEntry.COLUMN_PRICE_LEVEL + " TEXT, " +
                        PlacesEntry.COLUMN_PLACE_WANT_TO_SEE + " INTEGER);";
        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);

        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + ReviewsEntry.REVIEWS_TABLE_NAME + " (" +
                        ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewsEntry.COLUMN_REVIEW_PLACE_ID + " TEXT, " +
                        ReviewsEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                        ReviewsEntry.COLUMN_REVIEW_TEXT + " TEXT, " +
                        ReviewsEntry.COLUMN_REVIEW_RATING + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlacesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

//    public Cursor getPlaceFromTable(String query, String[] rows) {
//
//        String selection = ROW_NUMBER + "MATCH ?";
//        String[] selectionArgs = new String[]{query+ "*"};
//        return query(selection, selectionArgs,rows);
//    }
}
