package com.appcon.appconchatapp.listeners;

import com.appcon.appconchatapp.model.Story;

public interface StoriesItemListener {

    void OnAddStoryClicked();

    void OnViewStoryClicked(Story story);

}
