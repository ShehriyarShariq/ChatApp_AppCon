package com.appcon.appconchatapp.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.databinding.ActivityMainBinding;
import com.appcon.appconchatapp.databinding.ChatsFragmentBinding;
import com.appcon.appconchatapp.databinding.ChatsFragmentBindingImpl;
import com.appcon.appconchatapp.databinding.ChatsListSingleItemLayoutBinding;
import com.appcon.appconchatapp.databinding.FragmentConversationsBinding;
import com.appcon.appconchatapp.model.Conversation;
import com.appcon.appconchatapp.viewmodels.ConversationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChatsFragmentBinding chatsBinding = DataBindingUtil.inflate(inflater, R.layout.chats_fragment, container, false);
        List<Conversation> mConversationList = new ArrayList<>();
        mConversationList.add(new Conversation("Umar", "Hello", "Mon"));
        mConversationList.add(new Conversation("Shiryar", "Hi", "Tues"));
        mConversationList.add(new Conversation("Nustian", "Hiiii", "Mon"));


        chatsBinding.allChatsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatsBinding.allChatsList.setAdapter(new ConversationsAdapter(mConversationList));
        return chatsBinding.getRoot();
    }

    private class ConversationHolder extends RecyclerView.ViewHolder {

        ChatsListSingleItemLayoutBinding mBinding;

        public ConversationHolder(ChatsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new ConversationViewModel());
        }

        public void bind(Conversation conversation) {
            mBinding.getViewModel().setConversation(conversation);
            mBinding.executePendingBindings();
        }
    }

    private class ConversationsAdapter extends RecyclerView.Adapter<ConversationHolder> {
        private List<Conversation> mConversationList;

        public ConversationsAdapter(List<Conversation> conversationList) {
            mConversationList = conversationList;
        }

        @NonNull
        @Override
        public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ChatsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.chats_list_single_item_layout, parent, false);
            return new ConversationHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ConversationHolder holder, int position) {
            Conversation conversation = mConversationList.get(position);
            holder.bind(conversation);
        }

        @Override
        public int getItemCount() {
            return mConversationList.size();
        }
    }
}
