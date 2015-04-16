package com.atlassian.hipchat.interview.utils;

import android.util.Log;

import com.atlassian.hipchat.interview.model.UrlDetails;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Url manipulator, mainly to fetch details from a Url.
 *
 * I chose JSoup because it seems to be well established, though given some more time I would
 * make sure it is the right solution, as I have noticed very long times on the emulator and JUnit.
 * Both could be environmental issues though, so I don't rule out JSoup is just fine as well.
 * It could also still be some bug or mistake in the way I am using it... Some more time should be
 * spent analazing this solution and alternatives to retrieve the document's title.
 *
 */
public class UrlHelper
{
    private static final String UNKNOWN_TITLE = "Title Not Found";
    private static final String TAG = UrlHelper.class.getSimpleName();
    private static final int CONNECTION_TIMEOUT_MS = 3000;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    public static final String REFERRER = "http://www.google.com";

    public static UrlDetails fetchTitle(String url)
    {
        String urlTitle = UNKNOWN_TITLE;

        try
        {
            if (url.startsWith("www"))
            {
                url = "http://" + url;
            }
            Log.d(TAG, "connecting to " + url);

            // Forcing user agent to act like a computer browser, as I have noticed many websites
            // would give out a redirect page instead otherwise. One could argue we might want the
            // mobile version of the page, but I mostly disagree especially because of the redirect.
            final Connection connection = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .timeout(CONNECTION_TIMEOUT_MS);

            // Separating connect from get because it was being extremely slow in JUnit...
            // Definitely something to investigate on before shipping with JSoup
            Log.d(TAG, "connected to " + url);
            final Document doc = connection.get();

            Log.d(TAG, "Document found for " + url);
            urlTitle = doc.title();
        }
        catch (final Exception e)
        {
            Log.e(TAG, "Could not connect to " + url, e);
        }

        return new UrlDetails(url, urlTitle);
    }
}
