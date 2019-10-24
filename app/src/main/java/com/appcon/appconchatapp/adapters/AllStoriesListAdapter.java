package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.AllStoriesListSingleItemLayoutBinding;
import com.appcon.appconchatapp.listeners.StoriesItemListener;
import com.appcon.appconchatapp.model.Story;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AllStoriesListAdapter extends RecyclerView.Adapter<AllStoriesListAdapter.AllStoriesListViewHolder> {

    Activity activity;
    ArrayList<Story> stories;

    StoriesItemListener storiesItemListener;

    StorageReference firebaseStorage;

    public AllStoriesListAdapter(Activity activity, ArrayList<Story> stories, StoriesItemListener storiesItemListener) {
        this.activity = activity;
        this.stories = stories;

        this.storiesItemListener = storiesItemListener;

        firebaseStorage = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public AllStoriesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllStoriesListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.all_stories_list_single_item_layout, parent, false);
        return new AllStoriesListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllStoriesListViewHolder holder, int position) {
        Story story = stories.get(position);

        if(story.getStoryID().equals("add")){
            holder.binding.addLayout.setVisibility(View.VISIBLE);
        } else {
            holder.binding.othersLayout.setVisibility(View.VISIBLE);
            holder.binding.name.setText(story.getSenderName());

            String senderImageFileName = "dp_" + story.getSenderID() + ".jpg";

            StorageReference senderProfileImgURL = firebaseStorage.child("profile/" + senderImageFileName);

            Glide.with(activity)
                .load(senderProfileImgURL)
                .into(holder.binding.senderImage);
        }

        holder.binding.addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesItemListener.OnAddStoryClicked();
            }
        });

        holder.binding.othersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesItemListener.OnViewStoryClicked(stories.get(holder.getLayoutPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class AllStoriesListViewHolder extends RecyclerView.ViewHolder {

        AllStoriesListSingleItemLayoutBinding binding;

        public AllStoriesListViewHolder(@NonNull AllStoriesListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
