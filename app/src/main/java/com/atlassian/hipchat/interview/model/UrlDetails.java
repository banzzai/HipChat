package com.atlassian.hipchat.interview.model;

import com.google.gson.annotations.SerializedName;

public class UrlDetails
{
    @SerializedName("title") private String mTitle;
    @SerializedName("url") private String mUrl;

    public UrlDetails(String mUrl, String mTitle)
    {
        this.mUrl = mUrl;
        this.mTitle = mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
