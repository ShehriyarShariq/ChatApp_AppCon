package com.appcon.appconchatapp.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appcon.appconchatapp.adapters.AllStoriesListAdapter;
import com.appcon.appconchatapp.adapters.ConversationsListAdapter;
import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.PublicGroupsListAdapter;
import com.appcon.appconchatapp.databinding.ChatsFragmentBinding;
import com.appcon.appconchatapp.listeners.ChatsFragmentListener;
import com.appcon.appconchatapp.listeners.ConversationsListItemClickListener;
import com.appcon.appconchatapp.listeners.StoriesItemListener;
import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.model.PublicGroup;
import com.appcon.appconchatapp.model.Story;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    ChatsFragmentListener chatsFragmentListener;

    AllStoriesListAdapter allStoriesListAdapter;
    PublicGroupsListAdapter publicGroupsListAdapter;
    ConversationsListAdapter conversationsListAdapter;

    ArrayList<Story> stories;
    ArrayList<PublicGroup> publicGroups;
    ArrayList<Chat> conversations;

    ChatsFragmentBinding binding;

    ArrayList<Chat> selectedChats;

    public ChatsFragment(ChatsFragmentListener chatsFragmentListener) {
        this.chatsFragmentListener = chatsFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chats_fragment, container, false);

        stories = new ArrayList<>();
        publicGroups = new ArrayList<>();
        conversations = new ArrayList<>();
        selectedChats = new ArrayList<>();

        stories.add(new Story("add", "none", "none"));
        stories.add(new Story("add", "none", "none"));
        stories.add(new Story("add", "none", "none"));

        publicGroups.add(new PublicGroup("groupID", "Anime", "none"));
        publicGroups.add(new PublicGroup("groupID", "NSFW", "none"));
        publicGroups.add(new PublicGroup("groupID", "Taha", "none"));

        conversations.add(new Chat("chatID", "displayName", "lastMessageSeen", false, false, "UserID"));
        conversations.add(new Chat("chatID", "displayName", "lastMessageSeen", false, false, "UserID"));
        conversations.add(new Chat("chatID", "displayName", "lastMessageSeen", false, false, "UserID"));

        binding.allStoriesList.setHasFixedSize(true);
        binding.publicGroupsList.setHasFixedSize(true);
        binding.allChatsList.setHasFixedSize(true);

        LinearLayoutManager allStoriesListLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        allStoriesListLinearLayoutManager.setItemPrefetchEnabled(true);

        LinearLayoutManager publicGroupsListLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        publicGroupsListLinearLayoutManager.setItemPrefetchEnabled(true);

        LinearLayoutManager conversationsListLinearLayoutManager = new LinearLayoutManager(getActivity());
        conversationsListLinearLayoutManager.setItemPrefetchEnabled(true);

        binding.allStoriesList.setLayoutManager(allStoriesListLinearLayoutManager);
        binding.publicGroupsList.setLayoutManager(publicGroupsListLinearLayoutManager);
        binding.allChatsList.setLayoutManager(conversationsListLinearLayoutManager);

        allStoriesListAdapter = new AllStoriesListAdapter(getActivity(), stories, new StoriesItemListener() {
            @Override
            public void OnAddStoryClicked() {

            }

            @Override
            public void OnViewStoryClicked(Story story) {

            }
        });
        binding.allStoriesList.setAdapter(allStoriesListAdapter);

        publicGroupsListAdapter = new PublicGroupsListAdapter(getActivity(), publicGroups);
        binding.publicGroupsList.setAdapter(publicGroupsListAdapter);

        conversationsListAdapter = new ConversationsListAdapter(getActivity(), conversations, new ConversationsListItemClickListener() {
            @Override
            public void OnConversationClicked(int pos) {
                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                chatIntent.putExtra("chat", conversations.get(pos));
                startActivity(chatIntent);
            }

            @Override
            public void OnLongPress(int pos) {
                selectedChats.add(conversations.get(pos));
                chatsFragmentListener.OnChatLongPress(pos);
            }

            @Override
            public void updateSelected() {
                chatsFragmentListener.updateSelected();
            }

            @Override
            public void OnChatVideoBtnClicked(int pos) {

            }

            @Override
            public void OnChatAudioBtnClicked(int pos) {

            }

            @Override
            public void OnChatMuteBtnClicked(int pos) {

            }

            @Override
            public void OnChatDeleteBtnClicked(int pos) {

            }
        });

        binding.allChatsList.setAdapter(conversationsListAdapter);
        return binding.getRoot();
    }

    public void clearAllSelection() {
        conversationsListAdapter.notifyDataSetChanged();
//        binding.allChatsList.invalidate();
    }

    public ArrayList<Chat> getSelectedConvos(){
        return conversationsListAdapter.getSelectedConvos();
    }
}
