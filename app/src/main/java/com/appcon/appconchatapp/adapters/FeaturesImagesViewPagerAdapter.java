package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.appcon.appconchatapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FeaturesImagesViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Drawable> allImageResources;

    public FeaturesImagesViewPagerAdapter(Context context, ArrayList<Drawable> allImageResources) {
        this.context = context;
        this.allImageResources = allImageResources;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.features_pager_single_item_layout, parent, false);
        ImageView image = view.findViewById(R.id.img);

        image.setImageDrawable(allImageResources.get(position));

        parent.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return allImageResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
