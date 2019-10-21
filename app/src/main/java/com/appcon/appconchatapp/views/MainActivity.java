package com.appcon.appconchatapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appcon.appconchatapp.R;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPagerAdapter = new FragmentAdapter(getSupportFragmentManager());

        ViewPager pager = findViewById(R.id.view_pager);
        pager.setAdapter(mPagerAdapter);

    }

    private class FragmentAdapter extends FragmentPagerAdapter {


        public FragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            fragment = position == 0 ? new ChatsFragment() : new CallsFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}