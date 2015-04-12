package com.atlassian.hipchat.interview.model;

public class UrlDetails
{
    private String mUrl;
    private String mTitle;

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
