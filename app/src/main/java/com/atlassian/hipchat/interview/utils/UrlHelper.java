package com.atlassian.hipchat.interview.utils;

import android.util.Log;

import com.atlassian.hipchat.interview.model.UrlDetails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Url manipulator, mainly to fetch details from a Url
 */
public class UrlHelper
{
    private static final String UNKNOWN_TITLE = "Title Not Found";
    private static final String TAG = UrlHelper.class.getSimpleName();

    public static void fetchTitle(final String url, final UrlFetcherCallback callback)
    {
        Document doc = null;
        String urlTitle = UNKNOWN_TITLE;

        try
        {
            doc = Jsoup.connect(url).get();
            urlTitle = doc.title();
        }
        catch (final Exception e)
        {
            Log.e(TAG, "Could not connect to " + url);
        }

        callback.onUrlDetailsFetched(new UrlDetails(url, urlTitle));
    }

    public interface UrlFetcherCallback
    {
        public void onUrlDetailsFetched(final UrlDetails urlDetails);
    }
}
