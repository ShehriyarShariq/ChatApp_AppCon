package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ChatListSingleItemLayoutBinding;
import com.appcon.appconchatapp.model.AudioMessage;
import com.appcon.appconchatapp.model.ImageMessage;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.TextMessage;
import com.appcon.appconchatapp.model.VideoMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ChatMessagesListAdapter extends RecyclerView.Adapter<ChatMessagesListAdapter.ChatMessagesListViewHolder> {

    ArrayList<Message> messages;

    FirebaseAuth firebaseAuth;

    public ChatMessagesListAdapter(ArrayList<Message> messages) {
        this.messages = messages;

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatMessagesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_list_single_item_layout, parent, false);
        return new ChatMessagesListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessagesListViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message.getSenderID().equals(firebaseAuth.getCurrentUser().getUid())){
            holder.binding.self.setVisibility(View.VISIBLE);
            if(message instanceof TextMessage){
                TextMessage txtMsg = (TextMessage) message;
                holder.binding.textMsgSelf.setVisibility(View.VISIBLE);
                holder.binding.textSelf.setText(txtMsg.getText());
            } else if(message instanceof ImageMessage){
                ImageMessage imgMsg = (ImageMessage) message;
                holder.binding.imageMsgSelf.setVisibility(View.VISIBLE);

                

                holder.binding.imageSelf.

            } else if(message instanceof AudioMessage){
                AudioMessage audioMsg = (AudioMessage) message;
                holder.binding.audioSelf.setVisibility(View.VISIBLE);
            } else if(message instanceof VideoMessage){
//                VideoMessage vidMsg = (VideoMessage) message;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatMessagesListViewHolder extends RecyclerView.ViewHolder {

        private ChatListSingleItemLayoutBinding binding;

        public ChatMessagesListViewHolder(@NonNull ChatListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    public void refreshMessagesList(ArrayList<Message> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }
}
