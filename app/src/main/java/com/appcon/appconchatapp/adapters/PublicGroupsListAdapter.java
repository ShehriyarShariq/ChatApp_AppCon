package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.PublicGroupsListSingleItemLayoutBinding;
import com.appcon.appconchatapp.model.PublicGroup;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.DataUtils;

import java.util.ArrayList;

public class PublicGroupsListAdapter extends RecyclerView.Adapter<PublicGroupsListAdapter.PublicGroupsListViewHolder> {

    Activity activity;
    ArrayList<PublicGroup> publicGroups;

    public PublicGroupsListAdapter(Activity activity, ArrayList<PublicGroup> publicGroups) {
        this.activity = activity;
        this.publicGroups = publicGroups;
    }

    @NonNull
    @Override
    public PublicGroupsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PublicGroupsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.public_groups_list_single_item_layout, parent, false);
        return new PublicGroupsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicGroupsListViewHolder holder, int position) {
        PublicGroup group = publicGroups.get(position);

        holder.binding.groupName.setText(group.getGroupName());

        if(!group.getGroupImgUrl().equals("none")){
            Glide.with(activity)
                .load(group.getGroupImgUrl())
                .into(holder.binding.groupImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show more details dialog



            }
        });
    }

    @Override
    public int getItemCount() {
        return publicGroups.size();
    }

    public class PublicGroupsListViewHolder extends RecyclerView.ViewHolder {

        PublicGroupsListSingleItemLayoutBinding binding;

        public PublicGroupsListViewHolder(@NonNull PublicGroupsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
