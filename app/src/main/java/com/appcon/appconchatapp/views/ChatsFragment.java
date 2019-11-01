package com.appcon.appconchatapp.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import com.appcon.appconchatapp.model.ChatDB;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.MessageDB;
import com.appcon.appconchatapp.model.PublicGroup;
import com.appcon.appconchatapp.model.Story;
import com.appcon.appconchatapp.utils.ChatAppRepository;
import com.appcon.appconchatapp.viewmodels.ChatsFragmentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatsFragment extends Fragment {

    ChatsFragmentListener chatsFragmentListener;

    AllStoriesListAdapter allStoriesListAdapter;
    PublicGroupsListAdapter publicGroupsListAdapter;
    ConversationsListAdapter conversationsListAdapter;

    ArrayList<Story> stories;
    ArrayList<PublicGroup> publicGroups;
    ArrayList<Chat> conversations;

    ChatsFragmentBinding binding;
    ChatsFragmentViewModel viewModel;

    ArrayList<Chat> selectedChats;

    LiveData<ArrayList<String>> allChats;
    LiveData<Chat> chat;
    LiveData<MessageDB> messageOnline;
    ArrayList<String> observedChats;

    ChatAppRepository repository;
    LiveData<List<ChatDB>> chats;
    LiveData<List<MessageDB>> message;

    ArrayList<String> alreadyAddedMessageIDs;

    int index = 0;

    public ChatsFragment(ChatsFragmentListener chatsFragmentListener) {
        this.chatsFragmentListener = chatsFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chats_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(ChatsFragmentViewModel.class);

        viewModel.getAllConversations();
        repository = new ChatAppRepository(getActivity().getApplication());

        observedChats = new ArrayList<>();
        alreadyAddedMessageIDs = new ArrayList<>();

        allChats = viewModel.getAllChats();
        allChats.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> chatIDs) {
                if(chatIDs.size() > 0){
                    for(String chatID : chatIDs){
                        if(!observedChats.contains(chatID)){
                            viewModel.addChatsListener(chatID);
                            viewModel.addConversationListener(chatID);
                        }
                    }

                    observedChats.addAll(chatIDs);
                }
            }
        });

        stories = new ArrayList<>();
        publicGroups = new ArrayList<>();
        conversations = new ArrayList<>();
        selectedChats = new ArrayList<>();

        stories.add(new Story("add", "none", "none"));

        publicGroups.add(new PublicGroup("groupID", "Test Group", "none"));

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
                chatIntent.putExtra("chatID", conversations.get(pos).getChatID());
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

    @Override
    public void onResume() {
        super.onResume();

        chat = viewModel.getChat();
        chat.observe(this, new Observer<Chat>() {
            @Override
            public void onChanged(final Chat chat) {
                LiveData<ChatDB> chatDB = repository.getChat(chat.getChatID());
                chatDB.observe(getActivity(), new Observer<ChatDB>() {
                    @Override
                    public void onChanged(ChatDB chatDB) {
                        if(chatDB == null){
                            HashMap<String, String> permissions = chat.getPermissions();

                            ArrayList<String> admins = chat.getAdmins();
                            ArrayList<String> members = chat.getOtherUsers();

                            String adminsStr = "", membersStr = "";

                            for(int i = 0, n = admins.size(); i < n; i++){
                                adminsStr += admins.get(i);

                                if(i != (n - 1)){
                                    adminsStr += ",";
                                }
                            }

                            for(int i = 0, n = members.size(); i < n; i++){
                                membersStr += members.get(i);

                                if(i != (n - 1)){
                                    membersStr += ",";
                                }
                            }

                            chatDB = new ChatDB(
                                    chat.getChatID(),
                                    chat.getDisplayName(),
                                    chat.getDisplayPicture(),
                                    chat.getCreationDate(),
                                    chat.getDescription(),
                                    chat.getLastMessageSeenID(),
                                    String.valueOf(chat.isMuted()),
                                    String.valueOf(chat.isPinned()),
                                    permissions.get("adminWriteOnly"),
                                    permissions.get("adminSettingsEditOnly"),
                                    adminsStr,
                                    membersStr
                            );

                            repository.insertChat(chatDB);
                        }
                    }
                });
            }
        });

        messageOnline = viewModel.getMessage();
        messageOnline.observe(this, new Observer<MessageDB>() {
            @Override
            public void onChanged(final MessageDB message) {
                LiveData<ChatDB> chat = repository.getChat(message.getChatID());
                chat.observe(getActivity(), new Observer<ChatDB>() {
                    @Override
                    public void onChanged(ChatDB chatDB) {
                        if(chatDB != null){
                            final LiveData<List<MessageDB>> msgs = repository.getChatMessages(message.getChatID());
                            final ChatDB finalChatDB = chatDB;
                            msgs.observe(getActivity(), new Observer<List<MessageDB>>() {
                                @Override
                                public void onChanged(List<MessageDB> messageDBS) {
                                    if(messageDBS != null){
                                        boolean hasMessage = false;
                                        for(MessageDB messageDB : messageDBS){
                                            if(messageDB.getMessageID().equals(message.getMessageID())){
                                                hasMessage = true;
                                                break;
                                            }
                                        }

                                        if(!hasMessage){
                                            finalChatDB.setLastMessageSeenID(message.getMessageID());

                                            alreadyAddedMessageIDs.add(message.getMessageID());
                                            repository.insertMessages(message);
                                            repository.insertChat(finalChatDB);
                                        }

//                                        msgs.removeObserver(this);
                                    }
                                }
                            });
//                            if(!chatDB.getLastMessageSeenID().equals(message.getMessageID()) && !alreadyAddedMessageIDs.contains(message.getMessageID())){
//                                chatDB.setLastMessageSeenID(message.getMessageID());
//
//                                alreadyAddedMessageIDs.add(message.getMessageID());
//                                repository.insertMessages(message);
//                                repository.insertChat(chatDB);
//                            }
                        } else {
                            // Add Chat to database
                            chatDB = new ChatDB(
                                    message.getChatID(),
                                    "Shehriyar",
                                    "none",
                                    "none",
                                    "none",
                                    message.getMessageID(),
                                    "false",
                                    "false",
                                    "false",
                                    "false",
                                    "none",
                                    "none");
                            repository.insertMessages(message);
                            repository.insertChat(chatDB);
                        }
                    }
                });
            }
        });

        message = repository.getAllMessages();
        message.observe(this, new Observer<List<MessageDB>>() {
            @Override
            public void onChanged(List<MessageDB> messages) {
                if(messages.size() > 0){

                }
            }
        });

        chats = repository.getAllChats();
        chats.observe(this, new Observer<List<ChatDB>>() {
            @Override
            public void onChanged(final List<ChatDB> chatDBS) {
                conversations.clear();
                index = 0;
                for(int i = 0; i < chatDBS.size(); i++){
                    final ChatDB chat = chatDBS.get(i);
                    final LiveData<List<MessageDB>> msgs = repository.getChatMessages(chat.getChatID());
                    msgs.observe(getActivity(), new Observer<List<MessageDB>>() {
                        @Override
                        public void onChanged(List<MessageDB> messageDBS) {
                            if(messageDBS != null){
                                if(!messageDBS.isEmpty()){
                                    MessageDB lastMsg = messageDBS.get(messageDBS.size() - 1);

                                    HashMap<String, String> permissions = new HashMap<>();
                                    permissions.put("adminWriteOnly", chat.getAdminWriteOnly());
                                    permissions.put("adminSettingsEditOnly", chat.getAdminSettingsEditOnly());

                                    ArrayList<String> admins = new ArrayList<>();
                                    String[] adminsArr = chat.getAdmins().split(",");
                                    for(String adminID : adminsArr){
                                        admins.add(adminID);
                                    }

                                    ArrayList<String> members = new ArrayList<>();
                                    String[] membersArr = chat.getOtherUsers().split(",");
                                    for(String memberID : membersArr){
                                        members.add(memberID);
                                    }

                                    conversations.add(new Chat(
                                            chat.getChatID(),
                                            chat.getDisplayName(),
                                            chat.getDisplayPicture(),
                                            chat.getCreationDate(),
                                            chat.getDescription(),
                                            chat.getLastMessageSeenID(),
                                            lastMsg.getContent(),
                                            lastMsg.getTimeStamp(),
                                            lastMsg.getType(),
                                            Boolean.parseBoolean(chat.getMuted()),
                                            Boolean.parseBoolean(chat.getPinned()),
                                            permissions,
                                            admins,
                                            members
                                    ));

                                    if(index == (chatDBS.size() -1)){
                                        conversationsListAdapter.refreshConversations(conversations);
                                        msgs.removeObserver(this);
                                    } else {
                                        index++;
                                    }
                                } else {
                                    HashMap<String, String> permissions = new HashMap<>();
                                    permissions.put("adminWriteOnly", chat.getAdminWriteOnly());
                                    permissions.put("adminSettingsEditOnly", chat.getAdminSettingsEditOnly());

                                    ArrayList<String> admins = new ArrayList<>();
                                    String[] adminsArr = chat.getAdmins().split(",");
                                    for(String adminID : adminsArr){
                                        admins.add(adminID);
                                    }

                                    ArrayList<String> members = new ArrayList<>();
                                    String[] membersArr = chat.getOtherUsers().split(",");
                                    for(String memberID : membersArr){
                                        members.add(memberID);
                                    }

                                    conversations.add(new Chat(
                                            chat.getChatID(),
                                            chat.getDisplayName(),
                                            chat.getDisplayPicture(),
                                            chat.getCreationDate(),
                                            chat.getDescription(),
                                            chat.getLastMessageSeenID(),
                                            "Say Hi to your new friend",
                                            "None",
                                            "text",
                                            Boolean.parseBoolean(chat.getMuted()),
                                            Boolean.parseBoolean(chat.getPinned()),
                                            permissions,
                                            admins,
                                            members
                                    ));

                                    conversationsListAdapter.refreshConversations(conversations);
                                    msgs.removeObserver(this);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void clearAllSelection() {
        conversationsListAdapter.notifyDataSetChanged();
//        binding.allChatsList.invalidate();
    }

    public ArrayList<Chat> getSelectedConvos(){
        return conversationsListAdapter.getSelectedConvos();
    }

    public void setAllValidConversations() {

    }
}
