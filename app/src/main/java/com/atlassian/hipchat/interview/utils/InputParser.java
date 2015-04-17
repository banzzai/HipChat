package com.atlassian.hipchat.interview.utils;

import android.os.AsyncTask;

import com.atlassian.hipchat.interview.model.UrlDetails;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse input string into json.
 */
public class InputParser
{
    // Pattern for recognizing a URL, based off RFC 3986
    // see http://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static final Pattern emotePattern = Pattern.compile(
            "(\\([a-zA-Z_]*\\))",
            Pattern.MULTILINE | Pattern.DOTALL);

    private static final Pattern mentionPattern = Pattern.compile(
            "(@[a-zA-Z_]*)",
            Pattern.MULTILINE | Pattern.DOTALL);

    private static final String FTP_URL_DESCRIPTION = "Ftp url";

    final private ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(0, 10, 5l, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>());

    private ParserCallback mParseCallback;

    // All url details.
    ArrayList<UrlDetails> mUrls = new ArrayList<UrlDetails>();
    ArrayList<String> mMentions = new ArrayList<String>();
    ArrayList<String> mEmotes = new ArrayList<String>();

    // Keeps track of the number of Url we have fetched details about asynchronously.
    private int mUrlProcessed = 0;
    private int mTotalUrls = 0;
    private boolean mAllUrlsStarted = false;

    /**
     * Callback that will receive the parsed json when ready
     */
    public interface ParserCallback
    {
        public void onParsingComplete(final String jsonString);
    }

    /**
     * Main function to extract items from an input string
     * @param input String input to search through
     * @param callback callback that will be called when all the input is parsed
     */
    public void extractDetails(final String input, final ParserCallback callback)
    {
        mParseCallback = callback;

        extractMentions(input);
        extractEmotes(input);
        extractUrls(input);
    }

    /**
     * Pattern matching to find mentions
     * @param input String input to search through
     */
    private void extractMentions(final String input)
    {
        final Matcher matcher = mentionPattern.matcher(input);
        while (matcher.find())
        {
            mMentions.add(input.substring(matcher.start(1)+1, matcher.end()));
        }
    }

    /**
     * Pattern matching to find emotes
     * @param input String input to search through
     */
    private void extractEmotes(final String input)
    {
        final Matcher matcher = emotePattern.matcher(input);
        while (matcher.find())
        {
            mEmotes.add(input.substring(matcher.start(1)+1, matcher.end()-1));
        }
    }

    /**
     * Pattern matching to find urls
     * @param input String input to search through
     */
    private void extractUrls(final String input)
    {
        final Matcher matcher = urlPattern.matcher(input);

        while (matcher.find())
        {
            mTotalUrls++;
            final String url = input.substring(matcher.start(1), matcher.end());

            // We should be handling even more urls, like deep linking urls, smb://, etc.
            if (url.contains("ftp://"))
            {
                mUrlProcessed++;
                // We won't be trying to get a title for this url
                mUrls.add(new UrlDetails(url, FTP_URL_DESCRIPTION));
            }
            else
            {
                fetchTitle(url);
            }
        }

        mAllUrlsStarted = true;
        checkAndReturn();
    }

    /**
     * Connects to the url if possible and fetches its title from the document
     * @param url
     */
    private void fetchTitle(final String url)
    {
        new AsyncTask<String, Void, UrlDetails>()
        {
            @Override
            protected UrlDetails doInBackground(final String... url)
            {
                return UrlHelper.fetchTitle(url[0]);
            }

            @Override
            protected void onPostExecute(final UrlDetails details)
            {
                mUrls.add(details);
                mUrlProcessed++;
                checkAndReturn();
            }
        }.executeOnExecutor(mThreadPool, url);
    }

    private void checkAndReturn()
    {
        if (mUrlProcessed == mTotalUrls && mAllUrlsStarted)
        {
            mParseCallback.onParsingComplete(JsonSerializer.parseIntoJson(mMentions, mEmotes, mUrls));
        }
    }
}
