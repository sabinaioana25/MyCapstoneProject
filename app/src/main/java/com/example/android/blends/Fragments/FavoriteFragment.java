//package com.example.android.blends.Fragments;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.android.blends.R;
//
//public class FavoriteFragment extends Fragment {
//
////    private static final String TAG = FavoriteFragment.class.getSimpleName();
////    private static final int ID_FAVORITE_PLACE_LOADER = 10;
////    private static final int ID_CURSOR_FAVORITE_PLACE_LOADER = 11;
////
////    public RecyclerView recyclerViewFavorite;
////    public PlacesAdapter placesAdapterFavorite;
////    private GridLayoutManager gridLayoutManagerFavorite =
////            new GridLayoutManager(getContext(), 1);
////
////    private final String[] projectionFavorite = new String[]{
////            PlacesContract.PlacesEntry.COLUMN_PLACE_ID,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_ADDRESS,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_NUMBER,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_WEBSITE,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_RATING,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_LAT,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_LNG,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_IMAGE,
////            PlacesContract.PlacesEntry.COLUMN_OPENING_HOURS,
////            PlacesContract.PlacesEntry.COLUMN_PRICE_LEVEL,
////            PlacesContract.PlacesEntry.COLUMN_PLACE_WANT_TO_SEE
////    };
//
//    public FavoriteFragment() {
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
////        Stetho.initializeWithDefaults(getContext());
//        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
////
////        recyclerViewFavorite = rootView.findViewById(R.id.fav_places_list_recycler_view);
////        recyclerViewFavorite.setLayoutManager(gridLayoutManagerFavorite);
////        placesAdapterFavorite = new PlacesAdapter(this, getContext());
////        recyclerViewFavorite.setAdapter(placesAdapterFavorite);
////        getLoaderManager().initLoader(ID_FAVORITE_PLACE_LOADER, null, this);
//        return rootView;
//    }
//
////    @NonNull
////    @Override
////    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
//////        placesAdapterFavorite.deleteItemsInList();
//////        switch (i) {
//////            case ID_CURSOR_FAVORITE_PLACE_LOADER:
//////                return new CursorLoader(getActivity(), PlacesContract.PlacesEntry
//////                        .PLACES_CONTENT_URI,
//////                        projectionFavorite, null, null, null);
//////            case ID_FAVORITE_PLACE_LOADER:
//////                return new FavoritePlacesLoader(getContext(), NetworkUtils.buildPlaceListUrl
//////                        (getContext()).toString());
//////            default:
//////                return new FavoritePlacesLoader(getContext(), NetworkUtils.buildPlaceListUrl
//////                        (getContext()).toString());
//////        }
////    }
////
////    @Override
////    public void onLoadFinished(@NonNull Loader loader, Object object) {
//////        placesAdapterFavorite.deleteItemsInList();
//////        if (object instanceof Cursor) {
//////            Cursor cursor = (Cursor) object;
//////            placesAdapterFavorite.inserList(cursor);
//////        } else {
//////            List<PlaceModel> itemFavorite = (List<PlaceModel>) object;
//////            placesAdapterFavorite.inserList(itemFavorite);
//////        }
//////
//////        switch (loader.getId()) {
//////            case ID_CURSOR_FAVORITE_PLACE_LOADER:
//////                placesAdapterFavorite.deleteItemsInList();
//////                placesAdapterFavorite.inserList(object);
//////                break;
//////
//////            case ID_FAVORITE_PLACE_LOADER:
//////                List<PlaceModel> placeModelListFavorites = (List<PlaceModel>) object;
//////                placesAdapterFavorite.inserList(placeModelListFavorites);
//////                break;
//////        }
//////        placesAdapterFavorite.notifyDataSetChanged();
////    }
////
////    @Override
////    public void onLoaderReset(@NonNull Loader loader) {
//////        placesAdapterFavorite.inserList(null);
//////        switch (loader.getId()) {
//////            case ID_CURSOR_FAVORITE_PLACE_LOADER:
//////                placesAdapterFavorite.inserList(null);
//////                break;
//////            case ID_FAVORITE_PLACE_LOADER:
//////                placesAdapterFavorite.inserList(null);
//////                break;
//////        }
////    }
////
////    @Override
////    public void onPostResume(Loader loader) {
////        super.onResume();
////    }
////
////    @Override
////    public void onItemClick(String id) {
////        Intent goToDetailPage = new Intent(getContext(), DetailActivity.class);
////        goToDetailPage.putExtra("place id", id);
////        startActivity(goToDetailPage);
////    }
//}
