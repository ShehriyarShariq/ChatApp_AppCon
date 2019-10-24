package com.appcon.appconchatapp.model;

public class PublicGroup {

    String groupID, groupName, groupImgUrl;

    public PublicGroup(String groupID, String groupName, String groupImgUrl) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupImgUrl = groupImgUrl;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupImgUrl() {
        return groupImgUrl;
    }
}
