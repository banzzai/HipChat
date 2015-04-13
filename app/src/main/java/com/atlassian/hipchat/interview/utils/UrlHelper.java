package com.atlassian.hipchat.interview.utils;

import android.util.Log;

import com.atlassian.hipchat.interview.model.UrlDetails;

import org.jsoup.Connection;
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
    private static final int CONNECTION_TIMEOUT_MS = 3000;

    public static void fetchTitle(String url, final UrlFetcherCallback callback)
    {
        Document doc = null;
        String urlTitle = UNKNOWN_TITLE;

        try
        {
            if (url.startsWith("www"))
            {
                url = "http://" + url;
            }
            Log.e("LALA", "connect to " + url);

            Connection connection = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(CONNECTION_TIMEOUT_MS);
            Log.e("LALA", "connected to " + url);
            doc = connection.get();
            Log.e("LALA", "Document found for " + url);
            urlTitle = doc.title();
        }
        catch (final Exception e)
        {
            Log.e(TAG, "Could not connect to " + url, e);
        }

        callback.onUrlDetailsFetched(new UrlDetails(url, urlTitle));
    }

    public interface UrlFetcherCallback
    {
        public void onUrlDetailsFetched(final UrlDetails urlDetails);
    }
}
