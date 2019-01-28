package com.example.android.blends.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.blends.Utilities.JSONUtils;
import com.example.android.blends.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class DetailPlacesLoader extends AsyncTaskLoader<Object> {

    private static final String TAG = DetailPlacesLoader.class.getSimpleName();
    private final String id;
    int loaderId;

    public DetailPlacesLoader(@NonNull Context context, String id, int loaderId) {
        super(context);
        this.id = id;
        this.loaderId = loaderId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        URL url;
        url = NetworkUtils.buildDetailPlaceUrl(id);
        try {
            String detailPlaceJsonResponse = NetworkUtils.makeHttpRequest(url);
            return JSONUtils.extractPlaceDetailsFromJson(detailPlaceJsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
