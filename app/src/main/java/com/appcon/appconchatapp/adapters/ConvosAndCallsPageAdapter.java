package com.appcon.appconchatapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appcon.appconchatapp.listeners.ChatsFragmentListener;
import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.views.CallsFragment;
import com.appcon.appconchatapp.views.ChatsFragment;

import java.util.ArrayList;

public class ConvosAndCallsPageAdapter extends FragmentPagerAdapter {

    ChatsFragmentListener chatsFragmentListener;

    ChatsFragment chatsFragment;

    public ConvosAndCallsPageAdapter(@NonNull FragmentManager fm, ChatsFragmentListener chatsFragmentListener) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.chatsFragmentListener = chatsFragmentListener;

        chatsFragment = new ChatsFragment(chatsFragmentListener);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        fragment = position == 0 ? chatsFragment : new CallsFragment();

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void clearAllSelection(){
        chatsFragment.clearAllSelection();
    }

    public ArrayList<Chat> getSelectedConvos(){
        return chatsFragment.getSelectedConvos();
    }

}
