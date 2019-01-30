package com.example.android.blends.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.blends.Objects.PlaceModel;
import com.example.android.blends.R;
import com.example.android.blends.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends
        RecyclerView.Adapter<PlacesAdapter.PlacesMovieHolder> {

    private static final String TAG = PlacesAdapter.class.getSimpleName();
    private final PlacesDetailClickHandler onClickHandler;
    private final Context context;
    private final List<PlaceModel> placeList = new ArrayList<>();
    private Cursor cursor;
    private boolean mCursor = false;
    private static String cafeName;
    private String cafeAddress;
    private String priceLevel;
    private String cafeOpenNow;
    public static String cafeImage;

    public PlacesAdapter(PlacesDetailClickHandler onClickHandler, Context context) {
        this.onClickHandler = onClickHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacesMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,
                null);
        return new PlacesMovieHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesMovieHolder holder, int position) {

        cafeName = placeList.get(position).getName();
        cafeOpenNow = placeList.get(position).getOpenNow();
        cafeAddress = placeList.get(position).getAddress();
        priceLevel = placeList.get(position).getPriceLevel();

        holder.vhCafeName.setText(cafeName);
        holder.vhCafeOpenNow.setText(cafeOpenNow);
        holder.vhCafeAddress.setText(cafeAddress);
        holder.vhCafePriceLevel.setText(priceLevel);

        cafeImage = NetworkUtils.buildPlaceImage(placeList.get(position).getImage());
        Picasso.get()
                .load(cafeImage)
                .into(holder.vhCafeImage);
    }

    @Override
    public int getItemCount() {
        if (mCursor) {
            if (cursor != null) {
                return cursor.getCount();
            }
        } else {
            return placeList.size();
        }
        return 0;
    }

    public void insertList(Object data) {
        placeList.clear();
        if (data != null) {
            if (data instanceof Cursor) {
                this.cursor = (Cursor) data;
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                }
                mCursor = true;
            } else {
                mCursor = false;
                this.placeList.addAll((List<PlaceModel>) data);
            }
            notifyDataSetChanged();
        }
    }

    public void deleteItemsInList() {
        placeList.clear();
        notifyDataSetChanged();
    }

    public interface PlacesDetailClickHandler {

        void onItemClick(String id);
    }

    public class PlacesMovieHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView vhCafeName;
        private final TextView vhCafeOpenNow;
        private final TextView vhCafeAddress;
        private final TextView vhCafePriceLevel;
        private final ImageView vhCafeImage;

        PlacesMovieHolder(View view) {
            super(view);
            vhCafeName = view.findViewById(R.id.text_view_name);
            vhCafeOpenNow = view.findViewById(R.id.text_view_days);
            vhCafeAddress = view.findViewById(R.id.text_view_address_line_one);
            vhCafePriceLevel = view.findViewById(R.id.text_view_phone_number);
            vhCafeImage = view.findViewById(R.id.cafe_logo);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            String id;
            id = placeList.get(getAdapterPosition()).getPlaceId();
            onClickHandler.onItemClick(id);
        }
    }
}
