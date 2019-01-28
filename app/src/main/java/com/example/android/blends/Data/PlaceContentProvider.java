package com.example.android.blends.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.blends.Data.PlacesContract.PlacesEntry;

import static android.provider.BaseColumns._ID;
import static com.example.android.blends.Data.PlacesContract.PLACES_CONTENT_AUTHORITY;
import static com.example.android.blends.Data.PlacesContract.PLACES_PATH;
import static com.example.android.blends.Data.PlacesContract.PLACES_PATH_REVIEWS;
import static com.example.android.blends.Data.PlacesContract.PlacesEntry.TABLE_NAME;
import static com.example.android.blends.Data.PlacesContract.ReviewsEntry.REVIEWS_TABLE_NAME;

public class PlaceContentProvider extends ContentProvider {

    private static final int CODE_PLACES = 200;
    private static final int CODE_PLACES_ID = 201;
    private static final int CODE_PLACES_REVIEWS = 202;
    private static final String TAG = PlaceContentProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private PlacesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PLACES_CONTENT_AUTHORITY, PLACES_PATH, CODE_PLACES);
        uriMatcher.addURI(PLACES_CONTENT_AUTHORITY, PLACES_PATH +
                "/#", CODE_PLACES_ID);
        uriMatcher.addURI(PLACES_CONTENT_AUTHORITY, PLACES_PATH_REVIEWS,
                CODE_PLACES_REVIEWS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PlacesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String
                                sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case CODE_PLACES:
                cursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_PLACES_ID:
                selection = PlacesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_PLACES_REVIEWS:
                cursor = database.query(REVIEWS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Could not query URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE_PLACES:
                return PlacesContract.PLACES_CONTENT_LIST_TYPE;
            case CODE_PLACES_ID:
                return PlacesContract.PLACES_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id;

        switch (uriMatcher.match(uri)) {
            case CODE_PLACES:
                id = database.insert(TABLE_NAME, null,
                        values);
                if (id == -1) {
                    Log.e(TAG, "insert: Failed to insert row for " + uri);
                    return null;
                }
                break;
            case CODE_PLACES_REVIEWS:
                id = database.insert(REVIEWS_TABLE_NAME,
                        null,
                        values);
                if (id == -1) {
                    Log.e(TAG, "Failed to insert row for " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Insert is not possible for " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case CODE_PLACES:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_PLACES_ID:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_PLACES_REVIEWS:
                rowsDeleted = database.delete(REVIEWS_TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Could not delete row for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated;

        if (values == null || values.size() == 0) {
            return 0;
        }

        switch (uriMatcher.match(uri)) {
            case CODE_PLACES:
                rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_PLACES_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Could not update for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
