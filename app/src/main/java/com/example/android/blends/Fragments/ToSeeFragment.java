package com.example.android.blends.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.blends.Activities.DetailActivity;
import com.example.android.blends.Adapters.PlacesAdapter;
import com.example.android.blends.Data.PlacesContract;
import com.example.android.blends.Loaders.PlacesLoader;
import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.R;
import com.example.android.blends.Utilities.NetworkDetection;
import com.example.android.blends.Utilities.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.util.List;

public class ToSeeFragment extends Fragment
        implements LoaderManager.LoaderCallbacks,
        PlacesAdapter.PlacesDetailClickHandler {

    private static final String TAG = ToSeeFragment.class.getSimpleName();

    public static final int ID_PLACE_LOADER = 1;
    public static final int ID_CURSOR_PLACE_LOADER = 2;

    public RecyclerView recyclerViewToSee;
    public PlacesAdapter placesAdapter;
    public NetworkDetection networkDetection;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
    public static String placeName;
    PlaceModel placeModel = new PlaceModel();

    //    public List<PlaceModel> placeModelList = new ArrayList<PlaceModel>();
    private final String[] projection = new String[]{
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

    public ToSeeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Stetho.initializeWithDefaults(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_to_see, container, false);

        networkDetection = new NetworkDetection(getActivity());

        recyclerViewToSee = rootView.findViewById(R.id
                .places_list_recycler_view);
        recyclerViewToSee.setLayoutManager(gridLayoutManager);
        recyclerViewToSee.setHasFixedSize(true);
        placesAdapter = new PlacesAdapter(this, getContext());
        recyclerViewToSee.setAdapter(placesAdapter);

        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ID_PLACE_LOADER, null, this);
        } else {
            Log.v(TAG, "onCreateView: Loader not connected");
        }
        getLoaderManager().initLoader(ID_PLACE_LOADER, null, this);
        return rootView;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        placesAdapter.deleteItemsInList();
        switch (i) {
            case ID_CURSOR_PLACE_LOADER:
                return new CursorLoader(getActivity(), PlacesContract.PlacesEntry
                        .PLACES_CONTENT_URI, projection, null, null, null);
            case ID_PLACE_LOADER:
                return new PlacesLoader(getContext(), NetworkUtils.buildPlaceListUrl(getContext()
                ).toString());
            default:
                return new PlacesLoader(getContext(), NetworkUtils.buildPlaceListUrl(getContext()
                ).toString());
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        placesAdapter.deleteItemsInList();
        if (data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            placesAdapter.inserList(cursor);
        } else {
            List<PlaceModel> placeModelList = (List<PlaceModel>) data;
            placesAdapter.inserList(placeModelList);
        }
        switch (loader.getId()) {
            case ID_CURSOR_PLACE_LOADER:
                placesAdapter.deleteItemsInList();
                placesAdapter.inserList(data);
                break;
            case ID_PLACE_LOADER:
                List<PlaceModel>  placeModelList = (List<PlaceModel>) data;
                if (networkDetection.isConnected()) {
                    placesAdapter.inserList(placeModelList);

                } else {
                    return;
                }
                break;
        }
        placesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        placesAdapter.inserList(null);
        switch (loader.getId()) {
            case ID_CURSOR_PLACE_LOADER:
                placesAdapter.inserList(null);
            case ID_PLACE_LOADER:
                placesAdapter.inserList(null);
                break;
        }
    }

    @Override
    public void onPostResume(Loader loader) {
        super.onResume();
    }

    @Override
    public void onItemClick(String id) {
        Intent goToDetailActivity = new Intent(getContext(), DetailActivity.class);
        goToDetailActivity.putExtra("place id", id);
        startActivity(goToDetailActivity);
    }
}
