package com.example.android.blends.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.Utilities.JSONUtils;
import com.example.android.blends.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PlacesLoader extends AsyncTaskLoader<Object> {

    private static final String TAG = PlacesLoader.class.getSimpleName();
    private final String url;

    public PlacesLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<PlaceModel> loadInBackground() {
        URL url;
        url = NetworkUtils.buildPlaceListUrl(getContext());
        try {
            String placesJsonResponse = NetworkUtils.makeHttpRequest(url);
            return JSONUtils.extractFeaturesFromJson(placesJsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
