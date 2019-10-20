package com.appcon.appconchatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.appcon.appconchatapp.R;

import java.util.ArrayList;

public class FeaturesImagesViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Integer> allImageResources;

    LayoutInflater inflater;

    public FeaturesImagesViewPagerAdapter(Context context, ArrayList<Integer> allImageResources) {
        this.context = context;
        this.allImageResources = allImageResources;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return allImageResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.features_pager_single_item_layout, container, false);

        ImageView image = view.findViewById(R.id.img);
        image.setImageResource(allImageResources.get(position));

        container.addView(view);

        return view;
    }
}
