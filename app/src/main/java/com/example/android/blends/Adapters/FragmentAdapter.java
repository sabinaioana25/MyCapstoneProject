package com.example.android.blends.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.blends.Fragments.ToSeeFragment;
import com.example.android.blends.R;

public class FragmentAdapter extends FragmentPagerAdapter {
    public Context context;

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                return new ToSeeFragment();
//            case 1:
//                return new FavoriteFragment();
//        }
        return new ToSeeFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

//        switch (position) {
//            case 0:
//                return context.getString(R.string.fragment_want_to_see);
//            case 1:
//                return context.getString(R.string.fragment_favorites);
//        }
        return context.getString(R.string.fragment_want_to_see);

    }
}
