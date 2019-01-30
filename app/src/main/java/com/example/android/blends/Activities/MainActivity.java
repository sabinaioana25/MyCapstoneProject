package com.example.android.blends.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.blends.Adapters.PlacesAdapter;
import com.example.android.blends.Data.PlacesContract;
import com.example.android.blends.Loaders.PlacesLoader;
import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.R;
import com.example.android.blends.Utilities.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks,
        PlacesAdapter.PlacesDetailClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_PLACE_LOADER = 1000;
    private static final int ID_CURSOR_PLACE_LOADER = 1001;
    private static final String PLACE_ID = "place id";
    private Parcelable mListState;
    private static final String LIST_STATE_KEY = "savedState";

    private RecyclerView placesListRecyclerView;
    private PlacesAdapter placesListAdapter;
    private final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

    private final String[] listProjection = new String[]{
            PlacesContract.PlacesEntry.COLUMN_PLACE_ID,
            PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
            PlacesContract.PlacesEntry.COLUMN_PLACE_ADDRESS,
            PlacesContract.PlacesEntry.COLUMN_PLACE_NUMBER,
            PlacesContract.PlacesEntry.COLUMN_PLACE_WEBSITE,
            PlacesContract.PlacesEntry.COLUMN_PLACE_RATING,
            PlacesContract.PlacesEntry.COLUMN_PLACE_LAT,
            PlacesContract.PlacesEntry.COLUMN_PLACE_LNG,
            PlacesContract.PlacesEntry.COLUMN_PLACE_IMAGE,
            PlacesContract.PlacesEntry.COLUMN_OPENING_HOURS,
            PlacesContract.PlacesEntry.COLUMN_PRICE_LEVEL,
            PlacesContract.PlacesEntry.COLUMN_PLACE_WANT_TO_SEE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        Toolbar toolbar = findViewById(R.id.main_activity_id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        NestedScrollView scrollView = findViewById(R.id.main_activity_nested_scroll);
        scrollView.setFillViewport(true);

        placesListRecyclerView = findViewById(R.id.main_list_recycler_view);
        placesListRecyclerView.setLayoutManager(gridLayoutManager);
        placesListRecyclerView.setHasFixedSize(true);
        placesListAdapter = new PlacesAdapter(this, this);
        placesListRecyclerView.setAdapter(placesListAdapter);

        if (isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(ID_PLACE_LOADER, null, this);
        } else {
            Log.w(TAG, "onCreate: Loader not connected");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            gridLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }


    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        placesListAdapter.deleteItemsInList();
        switch (i) {
            case ID_CURSOR_PLACE_LOADER:
                return new CursorLoader(this, PlacesContract.PlacesEntry.PLACES_CONTENT_URI,
                        listProjection, null, null, null);
            case ID_PLACE_LOADER:
                return new PlacesLoader(this, NetworkUtils.buildPlaceListUrl(this).toString());
            default:
                return new PlacesLoader(this, NetworkUtils.buildPlaceListUrl(this).toString());
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        placesListAdapter.inserList(null);
        if (data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            placesListAdapter.inserList(cursor);
        } else {
            List<PlaceModel> list = (List<PlaceModel>) data;
            placesListAdapter.inserList(list);
        }
        switch (loader.getId()) {
            case ID_CURSOR_PLACE_LOADER:
                placesListAdapter.deleteItemsInList();
                placesListAdapter.inserList(data);
                break;
            case ID_PLACE_LOADER:
                List<PlaceModel> listPlaces = (List<PlaceModel>) data;
                if (isConnected()) {
                    placesListAdapter.inserList(listPlaces);
                } else {
                    return;
                }
                break;
        }
        placesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        placesListAdapter.inserList(null);
        switch (loader.getId()) {
            case ID_CURSOR_PLACE_LOADER:
                placesListAdapter.inserList(null);
            case ID_PLACE_LOADER:
                placesListAdapter.inserList(null);
                break;
        }
    }

    @Override
    public void onPostResume(Loader loader) {
    }

    @Override
    public void onItemClick(String id) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(PLACE_ID, id);
        startActivity(detailActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_view_map) {
            Intent goToMap = new Intent(this, MapActivity.class);
            startActivity(goToMap);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
