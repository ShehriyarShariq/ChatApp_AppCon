package com.appcon.appconchatapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appcon.appconchatapp.views.SecurityFeatureFrag;

public class FeaturesImagesViewPagerAdapter extends FragmentPagerAdapter {

    int numOfTabs;

    public FeaturesImagesViewPagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs) {
        super(fm, behavior);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new SecurityFeatureFrag();
            case 1:
                return new SecurityFeatureFrag();
            case 2:
                return new SecurityFeatureFrag();
        }

        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
