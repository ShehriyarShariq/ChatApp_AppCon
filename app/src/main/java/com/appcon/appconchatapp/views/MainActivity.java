package com.appcon.appconchatapp.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.appcon.appconchatapp.R;
import com.appcon.appconchatapp.adapters.ConvosAndCallsPageAdapter;
import com.appcon.appconchatapp.databinding.ActivityMainBinding;
import com.appcon.appconchatapp.listeners.ChatsFragmentListener;
import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.viewmodels.MainActivityViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ConvosAndCallsPageAdapter convosAndCallsPageAdapter;

    ChatsFragmentListener chatsFragmentListener;

    MainActivityViewModel viewModel;
    ActivityMainBinding binding;

    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        chatsFragmentListener = new ChatsFragmentListener() {
            @Override
            public void OnChatLongPress(int pos) {
                binding.defaultTopBar.setVisibility(View.GONE);
                binding.selectedChatTopBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnClearSelectionBtnPress() {

            }

            @Override
            public void updateSelected() {
                ArrayList<Chat> selectedConvos = convosAndCallsPageAdapter.getSelectedConvos();

                if(selectedConvos.size() == 0){
                    binding.defaultTopBar.setVisibility(View.VISIBLE);
                    binding.selectedChatTopBar.setVisibility(View.GONE);
                }

                binding.selectedCount.setText("" + selectedConvos.size());
            }
        };

        convosAndCallsPageAdapter = new ConvosAndCallsPageAdapter(getSupportFragmentManager(), chatsFragmentListener);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.selectedChatTopBar.setVisibility(View.GONE);
                binding.defaultTopBar.setVisibility(View.VISIBLE);

                convosAndCallsPageAdapter.clearAllSelection();
            }
        });

        binding.viewPager.setAdapter(convosAndCallsPageAdapter);

        currentPage = 0;

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_SETTLING){
                    if((binding.viewPager.getCurrentItem() == 0) && (currentPage == 1)){
                        currentPage = 0;
                        binding.tabSelector.setTranslationX(0);
                    } else if((binding.viewPager.getCurrentItem() == 1) && (currentPage == 0)){
                        currentPage = 1;
                        binding.tabSelector.setTranslationX(binding.tabSelector.getWidth() - convertDpToPx(20));
                    }
                }

            }
        });
    }

    public float convertDpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}