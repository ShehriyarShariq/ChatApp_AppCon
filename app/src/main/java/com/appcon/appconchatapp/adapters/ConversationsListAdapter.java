package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ChatsListSingleItemLayoutBinding;
import com.appcon.appconchatapp.listeners.ConversationsListItemClickListener;
import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.model.MessageDB;

import java.util.ArrayList;

public class ConversationsListAdapter extends RecyclerView.Adapter<ConversationsListAdapter.ConversationsListViewHolder> {

    private Activity activity;
    private ArrayList<Chat> conversations;
    private ArrayList<Chat> selectedConvos;

    ConversationsListItemClickListener conversationsListItemClickListener;

    public ConversationsListAdapter(Activity activity, ArrayList<Chat> conversations, ConversationsListItemClickListener conversationsListItemClickListener) {
        this.activity = activity;
        this.conversations = conversations;
        this.conversationsListItemClickListener = conversationsListItemClickListener;

        selectedConvos = new ArrayList<>();
    }

    @NonNull
    @Override
    public ConversationsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chats_list_single_item_layout, parent, false);
        return new ConversationsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConversationsListViewHolder holder, int position) {
        Chat chat = conversations.get(position);

        holder.binding.name.setText(chat.getDisplayName());
        holder.binding.lastMessageDay.setText(chat.getTime());

        switch (chat.getLastMessageSeenType()){
            case "text":
                holder.binding.lastMsgTxt.setVisibility(View.VISIBLE);
                holder.binding.lastMsgTxt.setText(chat.getLastMessageSeen());
                holder.binding.lastMsgImg.setVisibility(View.GONE);
                holder.binding.lastMsgAudio.setVisibility(View.GONE);
                holder.binding.lastMsgFile.setVisibility(View.GONE);
                break;
            case "image":
                holder.binding.lastMsgImg.setVisibility(View.VISIBLE);
                holder.binding.lastMsgTxt.setVisibility(View.GONE);
                holder.binding.lastMsgAudio.setVisibility(View.GONE);
                holder.binding.lastMsgFile.setVisibility(View.GONE);
                break;
            case "audio":
                holder.binding.lastMsgAudio.setVisibility(View.VISIBLE);
                holder.binding.lastMsgImg.setVisibility(View.GONE);
                holder.binding.lastMsgTxt.setVisibility(View.GONE);
                holder.binding.lastMsgFile.setVisibility(View.GONE);
                break;
            case "file":
                holder.binding.lastMsgFile.setVisibility(View.VISIBLE);
                holder.binding.lastMsgImg.setVisibility(View.GONE);
                holder.binding.lastMsgTxt.setVisibility(View.GONE);
                holder.binding.lastMsgAudio.setVisibility(View.GONE);
                break;
            case "game":
                break;
        }

        if(chat.isPinned()){
            holder.binding.isPinned.setVisibility(View.VISIBLE);
        } else {
            holder.binding.isPinned.setVisibility(View.INVISIBLE);
        }

        if(chat.isMuted()){
            holder.binding.muteIc.setVisibility(View.VISIBLE);
        } else {
            holder.binding.muteIc.setVisibility(View.GONE);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedConvos.size() > 0){
                    if(selectedConvos.contains(conversations.get(holder.getLayoutPosition()))){
                        holder.binding.itemOuter.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.binding.itemInner.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        selectedConvos.remove(conversations.get(holder.getLayoutPosition()));

                        conversationsListItemClickListener.updateSelected();
                    } else {
                        holder.binding.itemOuter.setCardBackgroundColor(activity.getResources().getColor(R.color.chat_list_item_select));
                        holder.binding.itemInner.setCardBackgroundColor(activity.getResources().getColor(R.color.chat_list_item_select_inner));
                        selectedConvos.add(conversations.get(holder.getLayoutPosition()));

                        conversationsListItemClickListener.updateSelected();
                    }
                } else {
                    conversationsListItemClickListener.OnConversationClicked(holder.getLayoutPosition());
                }
            }
        });

        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.binding.itemOuter.setCardBackgroundColor(activity.getResources().getColor(R.color.chat_list_item_select));
                holder.binding.itemInner.setCardBackgroundColor(activity.getResources().getColor(R.color.chat_list_item_select_inner));

                selectedConvos.add(conversations.get(holder.getLayoutPosition()));
                conversationsListItemClickListener.OnLongPress(holder.getLayoutPosition());

                conversationsListItemClickListener.updateSelected();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public ArrayList<Chat> getSelectedConvos(){
        return selectedConvos;
    }

    public class ConversationsListViewHolder extends RecyclerView.ViewHolder {

        private ChatsListSingleItemLayoutBinding binding;

        public ConversationsListViewHolder(@NonNull ChatsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public void refreshConversations(ArrayList<Chat> conversations){
        this.conversations = conversations;
        notifyDataSetChanged();
    }
}
