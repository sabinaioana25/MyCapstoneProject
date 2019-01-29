//package com.example.android.blends.Loaders;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.content.AsyncTaskLoader;
//import android.util.Log;
//
//import com.example.android.blends.Fragments.FavoriteFragment;
//import com.example.android.blends.Objects.PlaceModel;
//import com.example.android.blends.Utilities.JSONUtils;
//import com.example.android.blends.Utilities.NetworkUtils;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//
//public class FavoritePlacesLoader extends AsyncTaskLoader<Object> {
//
//    private static final String TAG = FavoriteFragment.class.getSimpleName();
//    public String favUrl;
//
//    public FavoritePlacesLoader(@NonNull Context context, String favUrl) {
//        super(context);
//        this.favUrl = favUrl;
//    }
//
//    @Override
//    protected void onStartLoading() {
//        super.onStartLoading();
//        forceLoad();
//    }
//
//    @Nullable
//    @Override
//    public List<PlaceModel> loadInBackground() {
//        URL favUrl;
//        favUrl = NetworkUtils.buildPlaceListUrl(getContext());
//        try {
//            String placesJsonResponse = NetworkUtils.makeHttpRequest(favUrl);
//            Log.e(TAG, "loadInBackground: " + placesJsonResponse);
//            return JSONUtils.extractFeaturesFromJson(placesJsonResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
