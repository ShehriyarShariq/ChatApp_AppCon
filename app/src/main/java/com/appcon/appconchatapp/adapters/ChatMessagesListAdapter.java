package com.appcon.appconchatapp.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ChatMessagesListAdapter extends RecyclerView.Adapter<ChatMessagesListAdapter.ChatMessagesListViewHolder> {

    Activity activity;
    ArrayList<Message> messages;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;

    public ChatMessagesListAdapter(Activity activity, ArrayList<Message> messages) {
        this.activity = activity;
        this.messages = messages;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
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
        if(message.getSenderID().equals(firebaseAuth.getCurrentUser().getUid())){ // Self message
            holder.binding.self.setVisibility(View.VISIBLE);
            if(message instanceof TextMessage){
                TextMessage txtMsg = (TextMessage) message;
                holder.binding.textMsgSelf.setVisibility(View.VISIBLE);
                holder.binding.textSelf.setText(txtMsg.getText());

                setViewBackground(holder.binding.textMsgSelf, message, position, false);

            } else if(message instanceof ImageMessage){
                ImageMessage imgMsg = (ImageMessage) message;
                holder.binding.imageMsgSelf.setVisibility(View.VISIBLE);

                StorageReference imgRef = firebaseStorage.getReferenceFromUrl(imgMsg.getImageURL());
                Glide.with(activity)
                        .load(imgRef)
                        .into(holder.binding.imageSelf);

                setViewBackground(holder.binding.imageMsgSelf, message, position, false);

            } else if(message instanceof AudioMessage){
                AudioMessage audioMsg = (AudioMessage) message;
                holder.binding.audioSelf.setVisibility(View.VISIBLE);

                DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(audioMsg.getAudioURL());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                downloadManager.enqueue(request);

                setViewBackground(holder.binding.audioSelf, message, position, false);

            } else if(message instanceof VideoMessage){
//                VideoMessage vidMsg = (VideoMessage) message;
            }

            if((position > 0) && !messages.get(position - 1).getSenderID().equals(firebaseAuth.getCurrentUser().getUid())){
                Resources r = activity.getResources();
                int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,r.getDisplayMetrics());

                LinearLayout.LayoutParams selfParams = (LinearLayout.LayoutParams) holder.binding.self.getLayoutParams();
                selfParams.setMargins(selfParams.leftMargin, top, selfParams.rightMargin, selfParams.bottomMargin);
                holder.binding.self.setLayoutParams(selfParams);
            } else {
                Resources r = activity.getResources();
                int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2,r.getDisplayMetrics());

                LinearLayout.LayoutParams selfParams = (LinearLayout.LayoutParams) holder.binding.self.getLayoutParams();
                selfParams.setMargins(selfParams.leftMargin, top, selfParams.rightMargin, selfParams.bottomMargin);
                holder.binding.self.setLayoutParams(selfParams);
            }

        } else {
            holder.binding.others.setVisibility(View.VISIBLE);

            if((position > 0) && !messages.get(position - 1).getSenderID().equals(message.getSenderID())){
                holder.binding.otherImg.setVisibility(View.VISIBLE);

                holder.binding.otherDetails.setVisibility(View.VISIBLE);
                holder.binding.otherDetailsNotSaved.setVisibility(View.VISIBLE);

                holder.binding.otherNumber.setText(message.getSenderPhoneNumber());
                holder.binding.otherAltName.setText("~" + message.getSenderName());

                Resources r = activity.getResources();
                int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,r.getDisplayMetrics());

                LinearLayout.LayoutParams otherParams = (LinearLayout.LayoutParams) holder.binding.others.getLayoutParams();
                otherParams.setMargins(otherParams.leftMargin, top, otherParams.rightMargin, otherParams.bottomMargin);
                holder.binding.others.setLayoutParams(otherParams);
            } else {
                Resources r = activity.getResources();
                int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2,r.getDisplayMetrics());

                LinearLayout.LayoutParams otherParams = (LinearLayout.LayoutParams) holder.binding.others.getLayoutParams();
                otherParams.setMargins(otherParams.leftMargin, top, otherParams.rightMargin, otherParams.bottomMargin);
                holder.binding.others.setLayoutParams(otherParams);
            }

            if(message instanceof TextMessage){
                TextMessage txtMsg = (TextMessage) message;
                holder.binding.textMsgOther.setVisibility(View.VISIBLE);
                holder.binding.textOther.setText(txtMsg.getText());

                setViewBackground(holder.binding.textMsgOther, message, position, true);

            } else if(message instanceof ImageMessage){
                ImageMessage imgMsg = (ImageMessage) message;
                holder.binding.imageLayoutOther.setVisibility(View.VISIBLE);

                StorageReference imgRef = firebaseStorage.getReferenceFromUrl(imgMsg.getImageURL());
                Glide.with(activity)
                        .load(imgRef)
                        .into(holder.binding.imageOther);

                setViewBackground(holder.binding.imageLayoutOther, message, position, true);

            } else if(message instanceof AudioMessage){
                AudioMessage audioMsg = (AudioMessage) message;
                holder.binding.audioOther.setVisibility(View.VISIBLE);

                DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(audioMsg.getAudioURL());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                downloadManager.enqueue(request);

                setViewBackground(holder.binding.audioOther, message, position, true);

            } else if(message instanceof VideoMessage){
//                VideoMessage vidMsg = (VideoMessage) message;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ChatMessagesListViewHolder extends RecyclerView.ViewHolder {

        private ChatListSingleItemLayoutBinding binding;

        public ChatMessagesListViewHolder(@NonNull ChatListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    private void setViewBackground(View view, Message message, int position, boolean other){
        if(messages.size() > 1){
            if(((position - 1) >= 0) && ((position + 1) < messages.size())){
                if((!messages.get(position - 1).getSenderID().equals(message.getSenderID())) && (messages.get(position + 1).getSenderID().equals(message.getSenderID()))){
                    // top other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_top_other : R.drawable.msg_bubble_top_self);
                } else if((messages.get(position - 1).getSenderID().equals(message.getSenderID())) && (messages.get(position + 1).getSenderID().equals(message.getSenderID()))){
                    // Mid other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_mid_other : R.drawable.msg_bubble_mid_self);

                } else if((messages.get(position - 1).getSenderID().equals(message.getSenderID())) && !(messages.get(position + 1).getSenderID().equals(message.getSenderID()))){
                    // Bottom other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_bot_other : R.drawable.msg_bubble_bot_self);
                } else {
                    // default other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_other : R.drawable.msg_bubble_self);
                }
            } else if(position == 0){
                if(messages.get(position + 1).getSenderID().equals(message.getSenderID())){
                    // top other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_top_other : R.drawable.msg_bubble_top_self);
                } else {
                    // default other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_other : R.drawable.msg_bubble_self);
                }
            } else if(position == (messages.size() - 1)){
                if(messages.get(position - 1).getSenderID().equals(message.getSenderID())){
                    // Bottom other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_bot_other : R.drawable.msg_bubble_bot_self);
                } else {
                    // default other bubble
                    view.setBackgroundResource(other ? R.drawable.msg_bubble_other : R.drawable.msg_bubble_self);
                }
            }
        } else {
            // default other bubble
            view.setBackgroundResource(other ? R.drawable.msg_bubble_other : R.drawable.msg_bubble_self);
        }

    }

    public void refreshMessagesList(ArrayList<Message> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }
}
